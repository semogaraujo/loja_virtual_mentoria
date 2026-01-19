package br.mentoria.lojavirtual.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.mentoria.lojavirtual.service.ImplementacaoUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // agora funciona com Spring Security 6
public class WebConfigSecurity {	

	private final ImplementacaoUserDetailsService implementacaoUserDetailsService;	

    /*Injeta a dependência via construtor*/
	public WebConfigSecurity(ImplementacaoUserDetailsService implementacaoUserDetailsService) {		
		this.implementacaoUserDetailsService = implementacaoUserDetailsService;
	}
    
	@Bean
    public PasswordEncoder passwordEncoder() {
		
        return new BCryptPasswordEncoder();
    }

	/* Registra o UserDetailsService como bean */ 
	@Bean 
	public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() { 
	
		return implementacaoUserDetailsService; 	
	}
	
	/* AuthenticationManager já sabe usar o UserDetailsService registrado */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
    	
        return auth.getAuthenticationManager();
    }

	/*Ignora as URLs, que ficam livres de autenticação*/
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/salvarAcesso", "/deleteAcesso").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }
}
