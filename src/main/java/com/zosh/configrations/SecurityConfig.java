// ============================================================================
// PARA TODOS LOS MICROSERVICIOS (User 8081, Salon 8082, Booking 8083, Category 8084)
// SecurityConfig.java UNIFICADO - UNA SOLA CONFIGURACIÓN CORS
// ============================================================================
package com.zosh.configrations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ENDPOINTS PÚBLICOS
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/error").permitAll()
                        // Microservicio específico endpoints públicos
                        .requestMatchers("/api/categories").permitAll() // Categorías públicas
                        .requestMatchers("/api/salons").permitAll() // Salones públicos para búsqueda
                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 🚨 CONFIGURACIÓN CORS SIMPLE Y CLARA
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

// ============================================================================
// 🚨 IMPORTANTE: ELIMINAR CUALQUIER OTRO ARCHIVO CorsConfig.java EN
// MICROSERVICIOS
// ============================================================================

/*
 * SI TIENES ARCHIVOS CorsConfig.java SEPARADOS EN LOS MICROSERVICIOS,
 * ELIMÍNALOS:
 * - src/main/java/com/zosh/configrations/CorsConfig.java ← ELIMINAR
 * - o cualquier otro CorsConfig.java ← ELIMINAR
 * 
 * SOLO debe existir la configuración CORS en este SecurityConfig.java
 */