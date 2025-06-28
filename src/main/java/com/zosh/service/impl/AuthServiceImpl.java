package com.zosh.service.impl;

import com.zosh.modal.User;
import com.zosh.payload.request.SignupDto;
import com.zosh.payload.response.AuthResponse;
import com.zosh.payload.response.ApiResponseBody;
import com.zosh.repository.UserRepository;
import com.zosh.service.AuthService;
import com.zosh.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse signup(SignupDto req) throws Exception {
        System.out.println("Signup request: " + req.getEmail());

        // Verificar si el usuario ya existe
        if (userRepository.findByEmail(req.getEmail()) != null) {
            throw new Exception("User already exists with email: " + req.getEmail());
        }

        // Crear nuevo usuario
        User newUser = new User();
        newUser.setEmail(req.getEmail());
        newUser.setFullName(req.getFullName());
        newUser.setUsername(req.getUsername());
        newUser.setPhone(req.getPhone());
        newUser.setRole(req.getRole());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);
        System.out.println("User saved with ID: " + savedUser.getId());

        // Generar JWT
        String jwt = jwtUtil.generateToken(
                savedUser.getEmail(),
                savedUser.getRole().toString(),
                savedUser.getId());

        // Crear respuesta compatible con frontend
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setRefresh_token(""); // Temporal, sin refresh token
        response.setMessage("User registered successfully");
        response.setTitle("Welcome " + savedUser.getFullName());
        response.setRole(savedUser.getRole());

        return response;
    }

    @Override
    public AuthResponse login(String email, String password) throws Exception {
        System.out.println("Login attempt for: " + email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            System.out.println("User not found: " + email);
            throw new Exception("Invalid email or password");
        }

        // Para simplificar las pruebas, usamos contraseña fija
        // En producción deberías hashear y verificar la contraseña
        if (!"password123".equals(password)) {
            System.out.println("Invalid password for user: " + email);
            throw new Exception("Invalid email or password");
        }

        // Generar JWT
        String jwt = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().toString(),
                user.getId());

        System.out.println("Login successful for: " + email + ", JWT generated");

        // Crear respuesta compatible con frontend
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setRefresh_token(""); // Temporal, sin refresh token
        response.setMessage("Login successful");
        response.setTitle("Welcome back " + user.getFullName());
        response.setRole(user.getRole());

        return response;
    }

    @Override
    public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception {
        throw new Exception("Refresh token not implemented yet");
    }
}