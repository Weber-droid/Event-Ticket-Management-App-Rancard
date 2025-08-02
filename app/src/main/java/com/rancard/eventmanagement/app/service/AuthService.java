package com.rancard.eventmanagement.app.service;

import com.rancard.eventmanagement.app.dto.AuthRequest;
import com.rancard.eventmanagement.app.dto.AuthResponse;
import com.rancard.eventmanagement.app.model.User;
import com.rancard.eventmanagement.app.model.UserRole;
import com.rancard.eventmanagement.app.repository.UserRepository;
import com.rancard.eventmanagement.app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ORGANIZER)
                .build();
        userRepository.save(user);
        var token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword())
        );

        var token = jwtUtil.generateToken(request.getUsername());
        return new AuthResponse(token);
    }
}
