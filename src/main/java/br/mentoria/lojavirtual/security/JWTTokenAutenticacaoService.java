package br.mentoria.lojavirtual.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

/*Criar a autenticação e retornar também a autenticação JWT*/
@Service
public class JWTTokenAutenticacaoService {

	/*Token de validade de 11 dias*/
	private static final long EXPIRATION_TIME = 959990000;
	
	/*Chave de senha para juntar com o JWT - pode ser qualquer valor*/
	private static final String SECRET = "ss/-*-*sdsk56049339-s/d-s*dsdds";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
	
	/*Gera o token e dá a resposta para o cliente com o JWT */
	public String generationToken(String username) throws Exception{	
		
		/*Montagem do Token*/
		return Jwts.builder() /*Chama o gerador de token*/
					.setSubject(username) /*Adiciona o user*/
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) 
					.signWith(key, SignatureAlgorithm.HS512)
					.compact(); /*Tempo de expiração*/
		
	}
	
	/*Adiciona o token no response */
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		
		String jwt = generationToken(username);			
		String token = TOKEN_PREFIX + " " + jwt;
		
		/*Resposta para a tela e para o cliente, outra API, navegador, aplicativo, javascript, outra chamada java*/
		/*Adiciona no header*/
		response.addHeader(HEADER_STRING, token);
		
		/*Retorna no body (útil para Postman/testes*/
		response.setContentType("application/json");
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");				
	}
	
	/* Valida o token e retorna o username*/
	public String validaToken(String token) {
		
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token.replace(TOKEN_PREFIX, "").trim())
				.getBody()
				.getSubject();		
	}
	
}
