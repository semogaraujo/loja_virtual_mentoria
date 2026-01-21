package br.mentoria.lojavirtual.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import br.mentoria.lojavirtual.model.Usuario;
import br.mentoria.lojavirtual.repository.UsuarioRepository;
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
	private static final long EXPIRATION_TIME = 11 * 24 * 60 * 60 * 1000L; // 11 dias
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String HEADER_STRING = "Authorization";

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

		String token = TOKEN_PREFIX + generationToken(username);

		/*
		 * Resposta para a tela e para o cliente, outra API, navegador, aplicativo,
		 * javascript, outra chamada java
		 */
		/* Adiciona no header */
		response.addHeader(HEADER_STRING, token);

		/* Retorna no body (útil para Postman/testes */
		response.setContentType("application/json");
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	/* Retorna o usuário válido com token ou caso não seja válido retorna null */
	public Authentication getAuthentication(HttpServletRequest request) {

		String token = request.getHeader(HEADER_STRING);

		if (token != null && token.startsWith(TOKEN_PREFIX)) {

			try {
				String username = Jwts.parserBuilder()
						.setSigningKey(key).build()
						.parseClaimsJws(token.replace(TOKEN_PREFIX, "").trim())
						.getBody()
						.getSubject(); /* ADMIN ou Vicente */

				if (username != null) {

					Usuario usuario = usuarioRepository.findByUsername(username);

					if (usuario != null) {

						return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getPassword(),
								usuario.getAuthorities());
					}
				}

			} catch (Exception e) {
				// token inválido ou expirado
				return null;
			}
		}
		return null;
	}
}
