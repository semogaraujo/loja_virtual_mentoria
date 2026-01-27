package br.mentoria.lojavirtual.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.mentoria.lojavirtual.model.Usuario;
import br.mentoria.lojavirtual.security.support.ErrorWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final JWTTokenAutenticacaoService jwtTokenService;

	public JWTLoginFilter(String url,
			org.springframework.security.authentication.AuthenticationManager authenticationManager,
			JWTTokenAutenticacaoService jwtTokenService) {
		super(new AntPathRequestMatcher(url, "POST"));
		setAuthenticationManager(authenticationManager);
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		try {

			UserDetails user = (UserDetails) authResult.getPrincipal();
			jwtTokenService.generationToken(user.getUsername());

			response.setContentType("application/json");
			// adiciona header e corpo com token
			jwtTokenService.addAuthentication(response, user.getUsername());
		} catch (Exception e) {
			ErrorWriter.writeJsonError(request, response, HttpStatus.INTERNAL_SERVER_ERROR, "SRV_500",
					"Falha ao gerar token.");
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		String msg = "Usuário ou senha inválidos.";
		ErrorWriter.writeJsonError(request, response, HttpStatus.UNAUTHORIZED, "AUTH_401", msg);
	}

}
