// package com.rancard.eventmanagement.app.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.rancard.eventmanagement.app.dto.EventRequest;
// import com.rancard.eventmanagement.app.model.Event;
// import com.rancard.eventmanagement.app.model.User;
// import com.rancard.eventmanagement.app.model.UserRole;
// import com.rancard.eventmanagement.app.repository.EventRepository;
// import com.rancard.eventmanagement.app.repository.UserRepository;
// import com.rancard.eventmanagement.app.security.JwtUtil;
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

// import java.time.LocalDateTime;

// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureWebMvc
// @ActiveProfiles("test")
// @Transactional
// class EventControllerIntegrationTest {

// @Autowired
// private MockMvc mockMvc;

// @Autowired
// private ObjectMapper objectMapper;

// @Autowired
// private EventRepository eventRepository;

// @Autowired
// private UserRepository userRepository;

// @Autowired
// private JwtUtil jwtUtil;

// @Autowired
// private PasswordEncoder passwordEncoder;

// private User user;
// private String jwtToken;
// private EventRequest eventRequest;

// @BeforeEach
// void setUp() {
// eventRepository.deleteAll();
// userRepository.deleteAll();

// // Create test user
// user = User.builder()
// .username("testuser")
// .password(passwordEncoder.encode("password123"))
// .role(UserRole.ORGANIZER)
// .build();
// user = userRepository.save(user);

// // Generate JWT token
// jwtToken = jwtUtil.generateToken(user.getUsername());

// // Create event request
// eventRequest = new EventRequest();
// eventRequest.setTitle("Test Event");
// eventRequest.setDescription("Test Description");
// eventRequest.setStartDate(LocalDateTime.now().plusDays(1));
// eventRequest.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));
// }

// @Test
// void createEvent_WithValidTokenAndData_ShouldReturnCreatedEvent() throws
// Exception {
// mockMvc.perform(post("/events")
// .header("Authorization", "Bearer " + jwtToken)
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(eventRequest)))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.title").value("Test Event"))
// .andExpect(jsonPath("$.description").value("Test Description"))
// .andExpect(jsonPath("$.ownerUsername").value("testuser"));
// }

// @Test
// void createEvent_WithoutToken_ShouldReturnUnauthorized() throws Exception {
// mockMvc.perform(post("/events")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(eventRequest)))
// .andExpect(status().isForbidden());
// }

// @Test
// void createEvent_WithInvalidToken_ShouldReturnUnauthorized() throws Exception
// {
// mockMvc.perform(post("/events")
// .header("Authorization", "Bearer invalid-token")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(eventRequest)))
// .andExpect(status().isForbidden());
// }

// @Test
// void getAllEvents_ShouldReturnEventsList() throws Exception {
// // Create test event
// Event event = Event.builder()
// .title("Test Event")
// .description("Test Description")
// .startDate(LocalDateTime.now().plusDays(1))
// .endDate(LocalDateTime.now().plusDays(1).plusHours(2))
// .owner(user)
// .build();
// eventRepository.save(event);

// mockMvc.perform(get("/events"))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$").isArray())
// .andExpect(jsonPath("$[0].title").value("Test Event"))
// .andExpect(jsonPath("$[0].description").value("Test Description"));
// }

// @Test
// void getEventById_WithValidId_ShouldReturnEvent() throws Exception {
// // Create test event
// Event event = Event.builder()
// .title("Test Event")
// .description("Test Description")
// .startDate(LocalDateTime.now().plusDays(1))
// .endDate(LocalDateTime.now().plusDays(1).plusHours(2))
// .owner(user)
// .build();
// event = eventRepository.save(event);

// mockMvc.perform(get("/events/" + event.getId()))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.title").value("Test Event"))
// .andExpect(jsonPath("$.description").value("Test Description"));
// }

// @Test
// void getEventById_WithInvalidId_ShouldReturnNotFound() throws Exception {
// mockMvc.perform(get("/events/999"))
// .andExpect(status().isBadRequest());
// }

// @Test
// void updateEvent_WithValidOwner_ShouldReturnUpdatedEvent() throws Exception {
// // Create test event
// Event event = Event.builder()
// .title("Test Event")
// .description("Test Description")
// .startDate(LocalDateTime.now().plusDays(1))
// .endDate(LocalDateTime.now().plusDays(1).plusHours(2))
// .owner(user)
// .build();
// event = eventRepository.save(event);

// eventRequest.setTitle("Updated Event");
// eventRequest.setDescription("Updated Description");

// mockMvc.perform(put("/events/" + event.getId())
// .header("Authorization", "Bearer " + jwtToken)
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(eventRequest)))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.title").value("Updated Event"))
// .andExpect(jsonPath("$.description").value("Updated Description"));
// }

// @Test
// void updateEvent_WithInvalidOwner_ShouldReturnForbidden() throws Exception {
// // Create different user
// User differentUser = User.builder()
// .username("differentuser")
// .password(passwordEncoder.encode("password123"))
// .role(UserRole.ORGANIZER)
// .build();
// differentUser = userRepository.save(differentUser);

// // Create event owned by different user
// Event event = Event.builder()
// .title("Test Event")
// .description("Test Description")
// .startDate(LocalDateTime.now().plusDays(1))
// .endDate(LocalDateTime.now().plusDays(1).plusHours(2))
// .owner(differentUser)
// .build();
// event = eventRepository.save(event);

// mockMvc.perform(put("/events/" + event.getId())
// .header("Authorization", "Bearer " + jwtToken)
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(eventRequest)))
// .andExpect(status().isBadRequest());
// }

// @Test
// void deleteEvent_WithValidOwner_ShouldReturnNoContent() throws Exception {
// // Create test event
// Event event = Event.builder()
// .title("Test Event")
// .description("Test Description")
// .startDate(LocalDateTime.now().plusDays(1))
// .endDate(LocalDateTime.now().plusDays(1).plusHours(2))
// .owner(user)
// .build();
// event = eventRepository.save(event);

// mockMvc.perform(delete("/events/" + event.getId())
// .header("Authorization", "Bearer " + jwtToken))
// .andExpect(status().isNoContent());
// }

// @Test
// void deleteEvent_WithoutToken_ShouldReturnUnauthorized() throws Exception {
// mockMvc.perform(delete("/events/1"))
// .andExpect(status().isForbidden());
// }
// }
