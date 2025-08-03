// package com.rancard.eventmanagement.app.service;

// import com.rancard.eventmanagement.app.dto.AuthRequest;
// import com.rancard.eventmanagement.app.dto.AuthResponse;
// import com.rancard.eventmanagement.app.model.User;
// import com.rancard.eventmanagement.app.model.UserRole;
// import com.rancard.eventmanagement.app.repository.UserRepository;
// import com.rancard.eventmanagement.app.security.JwtUtil;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class AuthServiceTest {

// @Mock
// private AuthenticationManager authenticationManager;

// @Mock
// private UserRepository userRepository;

// @Mock
// private PasswordEncoder passwordEncoder;

// @Mock
// private JwtUtil jwtUtil;

// @InjectMocks
// private AuthService authService;

// private AuthRequest authRequest;
// private User user;

// @BeforeEach
// void setUp() {
// authRequest = new AuthRequest();
// authRequest.setUsername("testuser");
// authRequest.setPassword("password123");

// user = User.builder()
// .id(1L)
// .username("testuser")
// .password("encodedPassword")
// .role(UserRole.ORGANIZER)
// .build();
// }

// @Test
// void register_WithNewUser_ShouldReturnAuthResponse() {
// // Given
// when(userRepository.existsByUsername(anyString())).thenReturn(false);
// when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
// when(userRepository.save(any(User.class))).thenReturn(user);
// when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");

// // When
// AuthResponse response = authService.register(authRequest);

// // Then
// assertNotNull(response);
// assertEquals("jwt-token", response.getToken());
// verify(userRepository).existsByUsername("testuser");
// verify(passwordEncoder).encode("password123");
// verify(userRepository).save(any(User.class));
// verify(jwtUtil).generateToken("testuser");
// }

// @Test
// void register_WithExistingUser_ShouldThrowException() {
// // Given
// when(userRepository.existsByUsername(anyString())).thenReturn(true);

// // When & Then
// RuntimeException exception = assertThrows(RuntimeException.class,
// () -> authService.register(authRequest));
// assertEquals("Username already exists", exception.getMessage());
// verify(userRepository).existsByUsername("testuser");
// verify(userRepository, never()).save(any(User.class));
// }

// @Test
// void login_WithValidCredentials_ShouldReturnAuthResponse() {
// // Given
// when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");

// // When
// AuthResponse response = authService.login(authRequest);

// // Then
// assertNotNull(response);
// assertEquals("jwt-token", response.getToken());
// verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
// verify(jwtUtil).generateToken("testuser");
// }

// @Test
// void login_WithInvalidCredentials_ShouldThrowException() {
// // Given
// when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
// .thenThrow(new BadCredentialsException("Invalid credentials"));

// // When & Then
// RuntimeException exception = assertThrows(RuntimeException.class,
// () -> authService.login(authRequest));
// assertEquals("Invalid username or password", exception.getMessage());
// verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
// verify(jwtUtil, never()).generateToken(anyString());
// }
// }
