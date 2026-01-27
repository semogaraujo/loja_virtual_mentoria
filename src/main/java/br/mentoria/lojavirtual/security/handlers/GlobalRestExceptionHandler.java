package br.mentoria.lojavirtual.security.handlers;

import java.io.IOException;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Conflict;
import org.springframework.web.servlet.NoHandlerFoundException;

import br.mentoria.lojavirtual.exception.LojaVirtualException;
import br.mentoria.lojavirtual.security.support.ErrorWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

	@ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
	public void handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		ErrorWriter.writeJsonError(req, res, HttpStatus.NOT_FOUND, "HTTP_404",
				"Endpoint não encontrado: " + ex.getHttpMethod() + " " + ex.getRequestURL());
	}

	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public void handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		String msg = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(java.util.stream.Collectors.joining("; "));
		ErrorWriter.writeJsonError(req, res, HttpStatus.BAD_REQUEST, "REQ_400", msg);
	}

	@ExceptionHandler(LojaVirtualException.class)
	public void handleLojaVirtualException(LojaVirtualException ex, HttpServletRequest req, HttpServletResponse res) throws IOException {

		ErrorWriter.writeJsonError(req, res, HttpStatus.CONFLICT, "REG_NEGOCIO", ex.getMessage());
	}

	@ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
	public void handleAuth(AuthenticationException ex, HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		ErrorWriter.writeJsonError(req, res, HttpStatus.UNAUTHORIZED, "AUTH_401", "Autenticação falhou.");
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public void handleDenied(AccessDeniedException ex, HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		ErrorWriter.writeJsonError(req, res, HttpStatus.FORBIDDEN, "AUTH_403", "Acesso negado.");
	}

	@ExceptionHandler(Exception.class)
	public void handleGeneric(Exception ex, HttpServletRequest req, HttpServletResponse res) throws IOException {
		ErrorWriter.writeJsonError(req, res, HttpStatus.INTERNAL_SERVER_ERROR, "SRV_500",
				"Erro interno do servidor. Se o problema persistir, contate o suporte.");
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public void handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		ErrorWriter.writeJsonError(req, res, HttpStatus.NOT_FOUND, "REQ_404", ex.getMessage());
	}
}
