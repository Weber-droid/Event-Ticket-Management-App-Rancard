// package com.rancard.eventmanagement.app.service;

// import com.rancard.eventmanagement.app.dto.TicketRequest;
// import com.rancard.eventmanagement.app.dto.TicketResponse;
// import com.rancard.eventmanagement.app.model.Event;
// import com.rancard.eventmanagement.app.model.Ticket;
// import com.rancard.eventmanagement.app.model.User;
// import com.rancard.eventmanagement.app.model.UserRole;
// import com.rancard.eventmanagement.app.repository.EventRepository;
// import com.rancard.eventmanagement.app.repository.TicketRepository;
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
// class TicketServiceTest {

// @Mock
// private TicketRepository ticketRepository;

// @Mock
// private EventRepository eventRepository;

// @Mock
// private UserRepository userRepository;

// @Mock
// private SecurityContext securityContext;

// @Mock
// private Authentication authentication;

// @InjectMocks
// private TicketService ticketService;

// private User user;
// private Event event;
// private Ticket ticket;
// private TicketRequest ticketRequest;

// @BeforeEach
// void setUp() {
// user = User.builder()
// .id(1L)
// .username("testuser")
// .password("password")
// .role(UserRole.CUSTOMER)
// .build();

// event = Event.builder()
// .id(1L)
// .title("Test Event")
// .description("Test Description")
// .startDate(LocalDateTime.now().plusDays(1))
// .endDate(LocalDateTime.now().plusDays(1).plusHours(2))
// .owner(user)
// .build();

// ticket = Ticket.builder()
// .id(1L)
// .code("TICKET-001")
// .event(event)
// .purchaser(user)
// .purchaseDate(LocalDateTime.now())
// .scanned(false)
// .build();

// ticketRequest = new TicketRequest();
// ticketRequest.setCode("TICKET-001");

// SecurityContextHolder.setContext(securityContext);
// when(securityContext.getAuthentication()).thenReturn(authentication);
// when(authentication.getName()).thenReturn("testuser");
// }

// @Test
// void createTicket_WithValidEventOwner_ShouldReturnTicketResponse() {
// // Given
// when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
// when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

// // When
// TicketResponse response = ticketService.createTicket(1L, ticketRequest);

// // Then
// assertNotNull(response);
// assertEquals("TICKET-001", response.getCode());
// assertFalse(response.isScanned());
// verify(eventRepository).findById(1L);
// verify(userRepository).findByUsername("testuser");
// verify(ticketRepository).save(any(Ticket.class));
// }

// @Test
// void createTicket_WithUnauthorizedUser_ShouldThrowException() {
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
// RuntimeException exception = assertThrows(RuntimeException.class,
// () -> ticketService.createTicket(1L, ticketRequest));
// assertEquals("Unauthorized", exception.getMessage());
// verify(eventRepository).findById(1L);
// verify(userRepository).findByUsername("testuser");
// verify(ticketRepository, never()).save(any(Ticket.class));
// }

// @Test
// void getTicketsForEvent_ShouldReturnListOfTickets() {
// // Given
// List<Ticket> tickets = Arrays.asList(ticket);
// when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
// when(ticketRepository.findByEvent(event)).thenReturn(tickets);

// // When
// List<TicketResponse> responses = ticketService.getTicketsForEvent(1L);

// // Then
// assertNotNull(responses);
// assertEquals(1, responses.size());
// assertEquals("TICKET-001", responses.get(0).getCode());
// verify(eventRepository).findById(1L);
// verify(ticketRepository).findByEvent(event);
// }

// @Test
// void purchaseTicket_WithAvailableTicket_ShouldReturnPurchasedTicket() {
// // Given
// ticket.setPurchaser(null); // Make ticket available
// when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
// when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
// when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

// // When
// TicketResponse response = ticketService.purchaseTicket(1L, 1L);

// // Then
// assertNotNull(response);
// assertEquals("TICKET-001", response.getCode());
// verify(ticketRepository).findById(1L);
// verify(ticketRepository).save(any(Ticket.class));
// }

// @Test
// void purchaseTicket_WithAlreadyPurchasedTicket_ShouldThrowException() {
// // Given
// when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket)); //
// ticket already has purchaser

// // When & Then
// RuntimeException exception = assertThrows(RuntimeException.class,
// () -> ticketService.purchaseTicket(1L, 1L));
// assertEquals("Ticket already purchased", exception.getMessage());
// verify(ticketRepository).findById(1L);
// verify(ticketRepository, never()).save(any(Ticket.class));
// }

// @Test
// void scanTicket_WithValidTicket_ShouldMarkAsScanned() {
// // Given
// when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
// when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

// // When
// TicketResponse response = ticketService.scanTicket(1L);

// // Then
// assertNotNull(response);
// verify(ticketRepository).findById(1L);
// verify(ticketRepository).save(any(Ticket.class));
// }

// @Test
// void scanTicket_WithNonExistentTicket_ShouldThrowException() {
// // Given
// when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

// // When & Then
// assertThrows(RuntimeException.class, () -> ticketService.scanTicket(1L));
// verify(ticketRepository).findById(1L);
// verify(ticketRepository, never()).save(any(Ticket.class));
// }
// }
