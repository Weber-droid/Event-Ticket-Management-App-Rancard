// package com.rancard.eventmanagement.app.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.rancard.eventmanagement.app.dto.AuthRequest;
// import com.rancard.eventmanagement.app.model.User;
// import com.rancard.eventmanagement.app.model.UserRole;
// import com.rancard.eventmanagement.app.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.transaction.annotation.Transactional;

// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureWebMvc
// @ActiveProfiles("test")
// @Transactional
// class AuthControllerIntegrationTest {

// @Autowired
// private MockMvc mockMvc;

// @Autowired
// private ObjectMapper objectMapper;

// @Autowired
// private UserRepository userRepository;

// @Autowired
// private PasswordEncoder passwordEncoder;

// private AuthRequest authRequest;

// @BeforeEach
// void setUp() {
// userRepository.deleteAll();

// authRequest = new AuthRequest();
// authRequest.setUsername("testuser");
// authRequest.setPassword("password123");
// }

// @Test
// void register_WithValidData_ShouldReturnJwtToken() throws Exception {
// mockMvc.perform(post("/auth/register")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(authRequest)))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.token").exists())
// .andExpect(jsonPath("$.token").isNotEmpty());
// }

// @Test
// void register_WithExistingUsername_ShouldReturnBadRequest() throws Exception
// {
// // Create existing user
// User existingUser = User.builder()
// .username("testuser")
// .password(passwordEncoder.encode("password123"))
// .role(UserRole.ORGANIZER)
// .build();
// userRepository.save(existingUser);

// mockMvc.perform(post("/auth/register")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(authRequest)))
// .andExpect(status().isBadRequest())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.error").value("Username already exists"));
// }

// @Test
// void register_WithInvalidData_ShouldReturnBadRequest() throws Exception {
// AuthRequest invalidRequest = new AuthRequest();
// // Empty username and password

// mockMvc.perform(post("/auth/register")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(invalidRequest)))
// .andExpect(status().isBadRequest());
// }

// @Test
// void login_WithValidCredentials_ShouldReturnJwtToken() throws Exception {
// // Create user first
// User user = User.builder()
// .username("testuser")
// .password(passwordEncoder.encode("password123"))
// .role(UserRole.ORGANIZER)
// .build();
// userRepository.save(user);

// mockMvc.perform(post("/auth/login")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(authRequest)))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.token").exists())
// .andExpect(jsonPath("$.token").isNotEmpty());
// }

// @Test
// void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception
// {
// // Create user with different password
// User user = User.builder()
// .username("testuser")
// .password(passwordEncoder.encode("differentpassword"))
// .role(UserRole.ORGANIZER)
// .build();
// userRepository.save(user);

// mockMvc.perform(post("/auth/login")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(authRequest)))
// .andExpect(status().isBadRequest())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.error").value("Invalid username or password"));
// }

// @Test
// void login_WithNonExistentUser_ShouldReturnUnauthorized() throws Exception {
// mockMvc.perform(post("/auth/login")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(authRequest)))
// .andExpect(status().isBadRequest())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.error").exists());
// }

// @Test
// void register_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
// mockMvc.perform(post("/auth/register")
// .contentType(MediaType.APPLICATION_JSON)
// .content("{}"))
// .andExpect(status().isBadRequest());
// }

// @Test
// void login_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
// mockMvc.perform(post("/auth/login")
// .contentType(MediaType.APPLICATION_JSON)
// .content("{}"))
// .andExpect(status().isBadRequest());
// }
// }
