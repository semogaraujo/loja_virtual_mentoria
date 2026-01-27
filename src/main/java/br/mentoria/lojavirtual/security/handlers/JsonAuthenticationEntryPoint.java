package br.mentoria.lojavirtual.security.handlers;


import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import br.mentoria.lojavirtual.security.support.ErrorWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		ErrorWriter.writeJsonError(request, response, HttpStatus.UNAUTHORIZED, "AUTH_401",
				"Você não está autenticado para acessar este recurso.");
	}
}

