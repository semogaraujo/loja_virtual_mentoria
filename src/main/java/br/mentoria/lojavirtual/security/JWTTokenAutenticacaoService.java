package br.mentoria.lojavirtual.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.mentoria.lojavirtual.model.Usuario;
import br.mentoria.lojavirtual.repository.UsuarioRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*Criar a autenticação e retornar também a autenticação JWT*/
@Service
public class JWTTokenAutenticacaoService {

	@Value("${jwt.secret}")
	private String secret;

	private Key key;

	private final UsuarioRepository usuarioRepository;

	public JWTTokenAutenticacaoService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	/* Token de validade de 11 dias */
	private static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 30 dias
	//private static final long EXPIRATION_TIME = 1;

	/* Gera o token JWT para o usuario */
	public String generationToken(String username) {

		/* Montagem do Token */
		return Jwts.builder() /* Chama o gerador de token */
				.setSubject(username) /* Adiciona o user */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS512).compact(); /* Tempo de expiração */

	}

	/* Adiciona o token no response */
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {

		String jwt = generationToken(username);
		String headerValue = "Bearer " + jwt;

		/* Adiciona no header */
		response.addHeader("Authorization", headerValue);

		if (!response.isCommitted()) {

			response.setStatus(HttpStatus.OK.value());
			response.setContentType("application/json;charset=UTF-8");

			String json = String.format("{\"Authorization\":\"%s\",\"tokenType\":\"Bearer\",\"expiresInMs\":%d}",
					headerValue, EXPIRATION_TIME);
			
			response.getWriter().write(json);

		}

	}

	public Authentication getAuthentication(String token) throws IOException {

		try {
			String username = Jwts.parserBuilder()
					.setSigningKey(key) // sua chave HMAC
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getSubject();

			if (username == null) {
				throw new BadCredentialsException("AUTH_002: Token JWT sem subject");
			}

			Usuario usuario = usuarioRepository.findByUsername(username);
			if (usuario == null) {
				throw new BadCredentialsException("AUTH_003: Usuário não encontrado");
			}

			return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getPassword(),
					usuario.getAuthorities());

		} catch (JwtException | IllegalArgumentException e) {
			// Assinatura inválida, token expirado, malformado, etc.
			throw new BadCredentialsException("AUTH_004: Token JWT inválido ou expirado");
		}
	}
}
