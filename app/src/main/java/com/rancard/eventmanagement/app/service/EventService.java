package com.rancard.eventmanagement.app.service;

import com.rancard.eventmanagement.app.dto.EventRequest;
import com.rancard.eventmanagement.app.dto.EventResponse;
import com.rancard.eventmanagement.app.dto.TicketResponse;
import com.rancard.eventmanagement.app.model.Event;
import com.rancard.eventmanagement.app.model.User;
import com.rancard.eventmanagement.app.repository.EventRepository;
import com.rancard.eventmanagement.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private final SseService sseService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    @CacheEvict(value = { "events", "insights" }, allEntries = true)
    public EventResponse createEvent(EventRequest request) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setOwner(getCurrentUser());
        Event saved = eventRepository.save(event);
        // return mapToResponse(saved);
        EventResponse response = mapToResponse(saved);
        sseService.sendUpdate(response);
        return response;
    }

    @CacheEvict(value = { "events", "insights" }, allEntries = true)
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id).orElseThrow();
        if (!event.getOwner().getUsername().equals(getCurrentUser().getUsername())) {
            throw new RuntimeException("Unauthorized");
        }
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        // return mapToResponse(eventRepository.save(event));
        EventResponse response = mapToResponse(eventRepository.save(event));
        sseService.sendUpdate(response);
        return response;

    }

    @CacheEvict(value = { "events", "insights" }, allEntries = true)
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        if (!event.getOwner().getUsername().equals(getCurrentUser().getUsername())) {
            throw new RuntimeException("Unauthorized");
        }
        eventRepository.delete(event);
    }

    @Cacheable("events")
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
