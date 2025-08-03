package com.rancard.eventmanagement.app.service;

import com.rancard.eventmanagement.app.dto.AuthRequest;
import com.rancard.eventmanagement.app.dto.AuthResponse;
import com.rancard.eventmanagement.app.model.User;
import com.rancard.eventmanagement.app.model.UserRole;
import com.rancard.eventmanagement.app.repository.UserRepository;
import com.rancard.eventmanagement.app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Login attempt for username: {}", request.getUsername());
        
        // Check if user exists
        var userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            log.warn("User not found: {}", request.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
        
        log.info("User found, attempting authentication...");
        
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );

            log.info("Authentication successful for user: {}", request.getUsername());
            var token = jwtUtil.generateToken(request.getUsername());
            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            log.error("Bad credentials for user: {}", request.getUsername());
            throw new RuntimeException("Invalid username or password");
        } catch (DisabledException e) {
            log.error("Account disabled for user: {}", request.getUsername());
            throw new RuntimeException("Account is disabled");
        } catch (LockedException e) {
            log.error("Account locked for user: {}", request.getUsername());
            throw new RuntimeException("Account is locked");
        } catch (Exception e) {
            log.error("Authentication failed for user: {} with error: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
