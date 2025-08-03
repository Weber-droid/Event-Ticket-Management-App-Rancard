// package com.rancard.eventmanagement.app.service;

// import com.rancard.eventmanagement.app.dto.EventRequest;
// import com.rancard.eventmanagement.app.dto.EventResponse;
// import com.rancard.eventmanagement.app.model.Event;
// import com.rancard.eventmanagement.app.model.User;
// import com.rancard.eventmanagement.app.model.UserRole;
// import com.rancard.eventmanagement.app.repository.EventRepository;
// import com.rancard.eventmanagement.app.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;

// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class EventServiceTest {

// @Mock
// private EventRepository eventRepository;

// @Mock
// private UserRepository userRepository;

// @Mock
// private SecurityContext securityContext;

// @Mock
// private Authentication authentication;

// @InjectMocks
// private EventService eventService;

// private User user;
// private Event event;
// private EventRequest eventRequest;

// @BeforeEach
// void setUp() {
// user = User.builder()
// .id(1L)
// .username("testuser")
// .password("password")
// .role(UserRole.ORGANIZER)
// .build();

// event = Event.builder()
// .id(1L)
// .title("Test Event")
// .description("Test Description")
// .startDate(LocalDateTime.now().plusDays(1))
// .endDate(LocalDateTime.now().plusDays(1).plusHours(2))
// .owner(user)
// .build();

// eventRequest = new EventRequest();
// eventRequest.setTitle("Test Event");
// eventRequest.setDescription("Test Description");
// eventRequest.setStartDate(LocalDateTime.now().plusDays(1));
// eventRequest.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));

// SecurityContextHolder.setContext(securityContext);
// when(securityContext.getAuthentication()).thenReturn(authentication);
// when(authentication.getName()).thenReturn("testuser");
// }

// @Test
// void createEvent_WithValidData_ShouldReturnEventResponse() {
// // Given
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
// when(eventRepository.save(any(Event.class))).thenReturn(event);

// // When
// EventResponse response = eventService.createEvent(eventRequest);

// // Then
// assertNotNull(response);
// assertEquals("Test Event", response.getTitle());
// assertEquals("Test Description", response.getDescription());
// assertEquals("testuser", response.getOwnerUsername());
// verify(userRepository).findByUsername("testuser");
// verify(eventRepository).save(any(Event.class));
// }

// @Test
// void createEvent_WithNonExistentUser_ShouldThrowException() {
// // Given
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

// // When & Then
// assertThrows(RuntimeException.class, () ->
// eventService.createEvent(eventRequest));
// verify(userRepository).findByUsername("testuser");
// verify(eventRepository, never()).save(any(Event.class));
// }

// @Test
// void getAllEvents_ShouldReturnListOfEvents() {
// // Given
// List<Event> events = Arrays.asList(event);
// when(eventRepository.findAll()).thenReturn(events);

// // When
// List<EventResponse> responses = eventService.getAllEvents();

// // Then
// assertNotNull(responses);
// assertEquals(1, responses.size());
// assertEquals("Test Event", responses.get(0).getTitle());
// verify(eventRepository).findAll();
// }

// @Test
// void getEventById_WithValidId_ShouldReturnEventResponse() {
// // Given
// when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

// // When
// EventResponse response = eventService.getEventById(1L);

// // Then
// assertNotNull(response);
// assertEquals("Test Event", response.getTitle());
// verify(eventRepository).findById(1L);
// }

// @Test
// void getEventById_WithInvalidId_ShouldThrowException() {
// // Given
// when(eventRepository.findById(1L)).thenReturn(Optional.empty());

// // When & Then
// assertThrows(RuntimeException.class, () -> eventService.getEventById(1L));
// verify(eventRepository).findById(1L);
// }

// @Test
// void updateEvent_WithValidOwner_ShouldReturnUpdatedEvent() {
// // Given
// when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
// when(eventRepository.save(any(Event.class))).thenReturn(event);

// eventRequest.setTitle("Updated Event");

// // When
// EventResponse response = eventService.updateEvent(1L, eventRequest);

// // Then
// assertNotNull(response);
// verify(eventRepository).findById(1L);
// verify(userRepository).findByUsername("testuser");
// verify(eventRepository).save(any(Event.class));
// }

// @Test
// void updateEvent_WithInvalidOwner_ShouldThrowException() {
// // Given
// User differentUser = User.builder()
// .id(2L)
// .username("differentuser")
// .password("password")
// .role(UserRole.ORGANIZER)
// .build();

// event.setOwner(differentUser);

// when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

// // When & Then
// assertThrows(RuntimeException.class, () -> eventService.updateEvent(1L,
// eventRequest));
// verify(eventRepository).findById(1L);
// verify(userRepository).findByUsername("testuser");
// verify(eventRepository, never()).save(any(Event.class));
// }

// @Test
// void deleteEvent_WithValidOwner_ShouldDeleteEvent() {
// // Given
// when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

// // When
// eventService.deleteEvent(1L);

// // Then
// verify(eventRepository).findById(1L);
// verify(userRepository).findByUsername("testuser");
// verify(eventRepository).delete(event);
// }

// @Test
// void deleteEvent_WithInvalidOwner_ShouldThrowException() {
// // Given
// User differentUser = User.builder()
// .id(2L)
// .username("differentuser")
// .password("password")
// .role(UserRole.ORGANIZER)
// .build();

// event.setOwner(differentUser);

// when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

// // When & Then
// assertThrows(RuntimeException.class, () -> eventService.deleteEvent(1L));
// verify(eventRepository).findById(1L);
// verify(userRepository).findByUsername("testuser");
// verify(eventRepository, never()).delete(any(Event.class));
// }
// }
