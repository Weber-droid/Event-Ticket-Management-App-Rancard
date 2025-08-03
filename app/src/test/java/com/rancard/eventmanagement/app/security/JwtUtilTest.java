// package com.rancard.eventmanagement.app.security;

// import io.jsonwebtoken.JwtException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.*;

// class JwtUtilTest {

// private JwtUtil jwtUtil;
// private final String testSecret =
// "test-secret-key-test-secret-key-test-secret-key";
// private final long testExpiration = 3600000; // 1 hour

// @BeforeEach
// void setUp() {
// jwtUtil = new JwtUtil(testSecret, testExpiration);
// }

// @Test
// void generateToken_WithValidUsername_ShouldReturnToken() {
// // Given
// String username = "testuser";

// // When
// String token = jwtUtil.generateToken(username);

// // Then
// assertNotNull(token);
// assertFalse(token.isEmpty());
// assertTrue(token.startsWith("eyJ")); // JWT tokens start with eyJ
// }

// @Test
// void extractUsername_WithValidToken_ShouldReturnUsername() {
// // Given
// String username = "testuser";
// String token = jwtUtil.generateToken(username);

// // When
// String extractedUsername = jwtUtil.extractUsername(token);

// // Then
// assertEquals(username, extractedUsername);
// }

// @Test
// void validateToken_WithValidToken_ShouldReturnTrue() {
// // Given
// String username = "testuser";
// String token = jwtUtil.generateToken(username);

// // When
// boolean isValid = jwtUtil.validateToken(token);

// // Then
// assertTrue(isValid);
// }

// @Test
// void validateToken_WithInvalidToken_ShouldReturnFalse() {
// // Given
// String invalidToken = "invalid.token.here";

// // When
// boolean isValid = jwtUtil.validateToken(invalidToken);

// // Then
// assertFalse(isValid);
// }

// @Test
// void validateToken_WithNullToken_ShouldReturnFalse() {
// // When
// boolean isValid = jwtUtil.validateToken(null);

// // Then
// assertFalse(isValid);
// }

// @Test
// void validateToken_WithEmptyToken_ShouldReturnFalse() {
// // When
// boolean isValid = jwtUtil.validateToken("");

// // Then
// assertFalse(isValid);
// }

// @Test
// void extractUsername_WithInvalidToken_ShouldThrowException() {
// // Given
// String invalidToken = "invalid.token.here";

// // When & Then
// assertThrows(JwtException.class, () ->
// jwtUtil.extractUsername(invalidToken));
// }

// @Test
// void generateToken_WithEmptyUsername_ShouldStillGenerateToken() {
// // Given
// String emptyUsername = "";

// // When
// String token = jwtUtil.generateToken(emptyUsername);

// // Then
// assertNotNull(token);
// assertFalse(token.isEmpty());

// String extractedUsername = jwtUtil.extractUsername(token);
// assertEquals(emptyUsername, extractedUsername);
// }

// @Test
// void generateToken_WithSpecialCharacters_ShouldHandleCorrectly() {
// // Given
// String usernameWithSpecialChars = "test@user.com";

// // When
// String token = jwtUtil.generateToken(usernameWithSpecialChars);

// // Then
// assertNotNull(token);
// String extractedUsername = jwtUtil.extractUsername(token);
// assertEquals(usernameWithSpecialChars, extractedUsername);
// }

// @Test
// void tokenLifecycle_ShouldWorkCorrectly() {
// // Given
// String username = "testuser";

// // When
// String token = jwtUtil.generateToken(username);
// String extractedUsername = jwtUtil.extractUsername(token);
// boolean isValid = jwtUtil.validateToken(token);

// // Then
// assertEquals(username, extractedUsername);
// assertTrue(isValid);
// }
// }
