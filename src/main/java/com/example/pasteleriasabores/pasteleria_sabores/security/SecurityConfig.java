package com.example.pasteleriasabores.pasteleria_sabores.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // Asegúrate que esta clase exista

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ⭐ RUTAS PÚBLICAS (LOGIN Y REGISTER)
                        .requestMatchers("/auth/**").permitAll()

                        // ⭐ RUTAS PÚBLICAS DE PRODUCTOS
                        .requestMatchers("/api/productos", "/api/productos/**").permitAll()

                        // ⭐ RUTAS PÚBLICAS DE CATEGORÍAS
                        .requestMatchers("/api/categorias", "/api/categorias/**").permitAll()

                        // ⭐ RUTAS PROTEGIDAS (PERFIL)
                        .requestMatchers("/api/usuarios/perfil/**").authenticated()

                        // ⭐ SOLO ADMIN
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/pedidos/**").hasAuthority("ADMIN")

                        // ⭐ Swagger permitido
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ⭐ Todo lo demás requiere autenticaciónn
                        .anyRequest().authenticated());

        // ⭐ ACTIVAR JWT
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
