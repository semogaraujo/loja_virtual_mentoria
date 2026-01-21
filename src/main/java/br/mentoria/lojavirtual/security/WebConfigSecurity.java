package br.mentoria.lojavirtual.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.mentoria.lojavirtual.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // agora funciona com Spring Security 6
public class WebConfigSecurity {

	private final ImplementacaoUserDetailsService implementacaoUserDetailsService;
	private final JWTTokenAutenticacaoService jwtTokenService;

	/* Injeta a dependência via construtor */
	public WebConfigSecurity(ImplementacaoUserDetailsService implementacaoUserDetailsService,
			JWTTokenAutenticacaoService jwtTokenService) {
		this.implementacaoUserDetailsService = implementacaoUserDetailsService;
		this.jwtTokenService = jwtTokenService;
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, DaoAuthenticationProvider authProvider) throws Exception {

        http.csrf(csrf -> csrf.disable())
        	// habilita suporte a CORS
            .cors(cors -> { })
            // Autorização
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index", "/login").permitAll()
                .anyRequest().authenticated()
            )
            // Stateless para JWT
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Exception handling simples (401/403)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> res.sendError(401))
                .accessDeniedHandler((req, res, e) -> res.sendError(403))
            )
            // Define explicitamente o provider (resolve o WARN dos dois UserDetailsService)
            .authenticationProvider(authProvider)

            // Filtro de login (gera token)
            .addFilterAfter(new JWTLoginFilter("/login", authenticationManager, jwtTokenService),
                    UsernamePasswordAuthenticationFilter.class)

            // Filtro de validação do token (para todas as requisições)
            .addFilterBefore(new JwtApiAutenticacaoFilter(jwtTokenService),
                    UsernamePasswordAuthenticationFilter.class)

            // Logout (se exposto)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
            );

        return http.build();
    }
	
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
	

    /* Provider explícito usando seu UserDetailsService + encoder */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
	
	/* AuthenticationManager já sabe usar o UserDetailsService registrado */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {

		return auth.getAuthenticationManager();
	}

	/* Fazendo a liberação contra erros de Cors nos navegadores */
	@Bean
	public CorsConfigurationSource corsConfigurationSource1() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*")); // ou lista de domínios confiáveis
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
