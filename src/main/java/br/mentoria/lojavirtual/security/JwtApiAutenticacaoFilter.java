package br.mentoria.lojavirtual.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.mentoria.lojavirtual.security.support.ErrorWriter;
import br.mentoria.lojavirtual.security.util.AuthorizationTokenResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtApiAutenticacaoFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtApiAutenticacaoFilter.class);

	private final JWTTokenAutenticacaoService jwtTokenService;
	private final UserDetailsService userDetailsService;

	public JwtApiAutenticacaoFilter(JWTTokenAutenticacaoService jwtTokenService,
			UserDetailsService userDetailsService) {
		this.jwtTokenService = jwtTokenService;
		this.userDetailsService = userDetailsService;
	}

	/** Evita filtrar /login e preflight CORS */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		String method = request.getMethod();
		return path != null && (path.endsWith("/login") || "OPTIONS".equalsIgnoreCase(method));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {		

		String traceId = MDC.get("traceId");
		
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
        }

		try {
			// 1) Extrai o token cru (sem "Bearer "), se houver
			String token = AuthorizationTokenResolver.resolveFromHeader(request);
			
			log.debug("[TRACE:{}][AUTH] Token (cru) recebido no filtro: {}",
                traceId,
                token != null
                        ? token.substring(0, Math.min(15, token.length())) + "..."
                        : null
            );

			// 2) Autentica somente se ainda não houver Authentication no contexto
			if (token != null && !token.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {

				// 3) O service agora devolve um Authentication pronto
				Authentication authentication = jwtTokenService.getAuthentication(token);
				if (authentication == null) {
					// Se seu service lança exceções quando inválido/expirado, este if pode ser
					// opcional
					throw new BadCredentialsException("AUTH_004: Token inválido ou expirado");
				}

				// 4) Grava no contexto de segurança
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			// 5) Segue a cadeia; exceções abaixo também serão mapeadas
			filterChain.doFilter(request, response);

		} catch (AuthenticationException e) {
			// 401: falha de autenticação (token inválido/expirado, etc.)
			SecurityContextHolder.clearContext();
			writeIfPossible(request, response, HttpStatus.UNAUTHORIZED, "AUTH_004",
					e.getMessage() != null ? e.getMessage() : "Token inválido ou expirado");

		} catch (AccessDeniedException e) {
			// 403: sem permissão
			SecurityContextHolder.clearContext();
			writeIfPossible(request, response, HttpStatus.FORBIDDEN, "AUTH_403", "Acesso negado.");

		} catch (ServletException e) {
			// Algumas exceções do chain vêm embrulhadas
			Throwable cause = e.getCause();
			if (cause instanceof AuthenticationException ae) {
				SecurityContextHolder.clearContext();
				writeIfPossible(request, response, HttpStatus.UNAUTHORIZED, "AUTH_004",
						ae.getMessage() != null ? ae.getMessage() : "Token inválido ou expirado");
			} else if (cause instanceof AccessDeniedException) {
				SecurityContextHolder.clearContext();
				writeIfPossible(request, response, HttpStatus.FORBIDDEN, "AUTH_403", "Acesso negado.");
			} else {
				SecurityContextHolder.clearContext();
				writeIfPossible(request, response, HttpStatus.INTERNAL_SERVER_ERROR, "SRV_500",
						"Erro interno do servidor.");
			}

		} catch (Exception e) {
			// 500: falhas inesperadas
			SecurityContextHolder.clearContext();
			writeIfPossible(request, response, HttpStatus.INTERNAL_SERVER_ERROR, "SRV_500",
					"Erro interno do servidor.");
		}
	}

	private void writeIfPossible(HttpServletRequest request, HttpServletResponse response, HttpStatus status,
			String code, String message) throws IOException {
		if (!response.isCommitted()) {
			ErrorWriter.writeJsonError(request, response, status, code, message);
		}
	}
}
