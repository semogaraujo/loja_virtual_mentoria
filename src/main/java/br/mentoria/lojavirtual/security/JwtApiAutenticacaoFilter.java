package br.mentoria.lojavirtual.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*Filtro onde todas as requisições serão capturadas para autenticar*/

public class JwtApiAutenticacaoFilter extends OncePerRequestFilter {

    private final JWTTokenAutenticacaoService jwtTokenService;

    public JwtApiAutenticacaoFilter(JWTTokenAutenticacaoService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        //IGNORA LOGIN
        if (path.endsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = jwtTokenService.getAuthentication(request);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //SEMPRE CONTINUA O FLUXO
        filterChain.doFilter(request, response);
    }
}
