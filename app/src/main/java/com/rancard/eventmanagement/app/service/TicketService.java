package com.rancard.eventmanagement.app.service;

import com.rancard.eventmanagement.app.dto.TicketRequest;
import com.rancard.eventmanagement.app.dto.TicketResponse;
import com.rancard.eventmanagement.app.model.Event;
import com.rancard.eventmanagement.app.model.Ticket;
import com.rancard.eventmanagement.app.model.User;
import com.rancard.eventmanagement.app.repository.EventRepository;
import com.rancard.eventmanagement.app.repository.TicketRepository;
import com.rancard.eventmanagement.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    public TicketResponse createTicket(Long eventId, TicketRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getOwner().getUsername().equals(getCurrentUser().getUsername())) {
            throw new RuntimeException("Unauthorized");
        }

        Ticket ticket = new Ticket();
        ticket.setCode(request.getCode());
        ticket.setEvent(event);
        return mapToResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> getTicketsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return ticketRepository.findByEvent(event)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse purchaseTicket(Long eventId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        if (ticket.getPurchaser() != null) {
            throw new RuntimeException("Ticket already purchased");
        }

        // Anonymous user purchase
        String username = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "guest";

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User guest = new User();
                    guest.setUsername(username);
                    guest.setPassword("N/A");
                    guest.setRole(null);
                    return userRepository.save(guest);
                });

        ticket.setPurchaser(user);
        ticket.setPurchaseDate(LocalDateTime.now());
        return mapToResponse(ticketRepository.save(ticket));
    }

    public TicketResponse scanTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setScanned(true);
        return mapToResponse(ticketRepository.save(ticket));
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        TicketResponse res = new TicketResponse();
        res.setId(ticket.getId());
        res.setCode(ticket.getCode());
        res.setScanned(ticket.isScanned());
        res.setPurchaseDate(ticket.getPurchaseDate());
        res.setPurchaserUsername(ticket.getPurchaser() != null ? ticket.getPurchaser().getUsername() : null);
        return res;
    }
}
