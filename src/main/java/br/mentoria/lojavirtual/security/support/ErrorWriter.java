package br.mentoria.lojavirtual.security.support;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.mentoria.lojavirtual.security.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public final class ErrorWriter {

	private static final ObjectMapper MAPPER = new ObjectMapper()
			 .registerModule(new JavaTimeModule())
			 .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


	private ErrorWriter() {}
	
	public static void writeJsonError(HttpServletRequest req, HttpServletResponse res, HttpStatus status, String code,
			String message) throws IOException {
		res.setStatus(status.value());
		res.setContentType("application/json;charset=UTF-8");
		

		String traceId = Optional.ofNullable(req.getHeader("X-Request-Id")).filter(s -> !s.isBlank())
				.orElse(UUID.randomUUID().toString());

		ErrorResponse body = new ErrorResponse(status.value(), status.getReasonPhrase(), code, message,
				req.getRequestURI(), req.getMethod(), traceId);

		MAPPER.writeValue(res.getWriter(), body);
		res.getWriter().flush();
	}

}
