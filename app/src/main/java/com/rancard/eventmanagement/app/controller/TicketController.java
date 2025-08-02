package com.rancard.eventmanagement.app.controller;

import com.rancard.eventmanagement.app.dto.TicketRequest;
import com.rancard.eventmanagement.app.dto.TicketResponse;
import com.rancard.eventmanagement.app.service.TicketService;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/events/{id}/tickets")
    public ResponseEntity<TicketResponse> createTicket(@PathVariable Long id,
                                                       @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.createTicket(id, request));
    }

    @GetMapping("/events/{id}/tickets")
    public ResponseEntity<List<TicketResponse>> getTickets(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketsForEvent(id));
    }

    @PostMapping("/events/{id}/tickets/{ticketId}/purchase")
    public ResponseEntity<TicketResponse> purchase(@PathVariable Long id,
                                                   @PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.purchaseTicket(id, ticketId));
    }

    @PostMapping("/tickets/{id}/scan")
    public ResponseEntity<TicketResponse> scan(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.scanTicket(id));
    }
}
