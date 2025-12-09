package com.example.pasteleriasabores.pasteleria_sabores.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "A9x!P3kz#Qw82@LmN7eT44vZbP0rHxC2KfD1sGmU8pR9yJqW0tV5bA3cN6fZ8";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // GENERAR TOKEN
    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("authorities", List.of(rol))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // EXTRAER EMAIL
    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // EXTRAER ROL
    public String extraerRol(String token) {
        Claims claims = extraerTodosLosClaims(token);
        List<String> roles = claims.get("authorities", List.class);

        return (roles != null && !roles.isEmpty()) ? roles.get(0) : null;
    }

    // VALIDAR TOKEN
    public Boolean validarToken(String token, String emailUsuario) {
        final String email = extraerEmail(token);
        return (email.equals(emailUsuario) && !estaExpirado(token));
    }

    private boolean estaExpirado(String token) {
        Date expiration = extraerClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
