package com.rancard.eventmanagement.app.service;

//import com.example.ticketing.dto.EventRequest;
//import com.example.ticketing.dto.EventResponse;
//import com.example.ticketing.dto.TicketResponse;
//import com.example.ticketing.model.Event;
//import com.example.ticketing.model.User;
//import com.example.ticketing.repository.EventRepository;
//import com.example.ticketing.repository.UserRepository;
import com.rancard.eventmanagement.app.dto.EventRequest;
import com.rancard.eventmanagement.app.dto.EventResponse;
import com.rancard.eventmanagement.app.dto.TicketResponse;
import com.rancard.eventmanagement.app.model.Event;
import com.rancard.eventmanagement.app.model.User;
import com.rancard.eventmanagement.app.repository.EventRepository;
import com.rancard.eventmanagement.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketService ticketService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    public EventResponse createEvent(EventRequest request) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setOwner(getCurrentUser());
        Event saved = eventRepository.save(event);

        return mapToResponse(saved);
    }

    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id).orElseThrow();
        if (!event.getOwner().getUsername().equals(getCurrentUser().getUsername())) {
            throw new RuntimeException("Unauthorized");
        }
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        return mapToResponse(eventRepository.save(event));
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        if (!event.getOwner().getUsername().equals(getCurrentUser().getUsername())) {
            throw new RuntimeException("Unauthorized");
        }
        eventRepository.delete(event);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setStartDate(event.getStartDate());
        response.setEndDate(event.getEndDate());
        response.setOwnerUsername(event.getOwner().getUsername());

        List<TicketResponse> tickets = ticketService.getTicketsForEvent(event.getId());
        response.setTickets(tickets);
        return response;
    }
}
