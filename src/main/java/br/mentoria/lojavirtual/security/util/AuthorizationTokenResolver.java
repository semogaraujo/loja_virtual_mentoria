package br.mentoria.lojavirtual.security.util;

import jakarta.servlet.http.HttpServletRequest;

public final class AuthorizationTokenResolver {

	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private AuthorizationTokenResolver() {
	}

	public static String resolveFromHeader(HttpServletRequest request) {
		String header = request.getHeader(AUTH_HEADER);
		if (header == null || header.isBlank()) {
			return null;
		}

		String value = header.trim();
		
		while (value.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
			value =  value.substring(BEARER_PREFIX.length()).trim();
		}
		
		return value;
	}
}
