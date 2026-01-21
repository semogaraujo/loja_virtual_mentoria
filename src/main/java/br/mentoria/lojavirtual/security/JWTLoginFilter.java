package br.mentoria.lojavirtual.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.mentoria.lojavirtual.model.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JWTTokenAutenticacaoService jwtTokenService;

    public JWTLoginFilter(String url, AuthenticationManager authenticationManager,
                          JWTTokenAutenticacaoService jwtTokenService) {
        super(new AntPathRequestMatcher(url, "POST"));
        setAuthenticationManager(authenticationManager);
        this.jwtTokenService = jwtTokenService;
    }

	/*Retorna o usuário ao processar a autenticação*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    	/*Obter o usuário*/
        Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        /*Retorna o usuário com login e senha*/
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        // Gera e adiciona JWT na resposta
        try {
        	
			jwtTokenService.addAuthentication(response, authResult.getName());
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
    }
}

