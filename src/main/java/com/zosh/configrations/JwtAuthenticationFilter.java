package com.zosh.configrations;

import com.zosh.service.UserService;
import com.zosh.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // Extraer JWT del header Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwt);
                System.out.println("JWT Filter - Email extracted: " + email);
            } catch (Exception e) {
                System.out.println("JWT Filter - Error extracting email: " + e.getMessage());
            }
        }

        // Si tenemos email y no hay autenticación previa
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Validar token
                if (jwtUtil.validateToken(jwt, email)) {
                    String role = jwtUtil.extractRole(jwt);
                    System.out.println("JWT Filter - Role extracted: " + role);

                    // Crear autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email, null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("JWT Filter - Authentication set for: " + email + " with role: " + role);
                } else {
                    System.out.println("JWT Filter - Token validation failed for: " + email);
                }
            } catch (Exception e) {
                System.out.println("JWT Filter - Authentication error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}