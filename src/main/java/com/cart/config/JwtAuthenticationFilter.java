package com.cart.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY = "miClaveSecretaSuperSeguraYCompleja1234567890@";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraer el token del encabezado Authorization
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.replace(TOKEN_PREFIX, "");

            try {
                // 2. Validar el token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))).build()
                        .parseClaimsJws(token).getBody();

                // 3. Obtener el username y roles del token
                String username = claims.getSubject();

                // (Si tienes roles, extraerlos aquí. Ejemplo:)
                // List<String> roles = claims.get("roles", List.class);
                List<SimpleGrantedAuthority> authorities = Collections.emptyList();

                // 4. Crear objeto de autenticación
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

                // 5. Establecer autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Si el token es inválido, limpiamos el contexto
                SecurityContextHolder.clearContext();
            }
        }

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}