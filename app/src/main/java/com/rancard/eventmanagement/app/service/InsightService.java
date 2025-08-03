package com.rancard.eventmanagement.app.service;

import com.rancard.eventmanagement.app.dto.InsightResponse;
import com.rancard.eventmanagement.app.repository.EventRepository;
import com.rancard.eventmanagement.app.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    @Cacheable("insights")
    public InsightResponse getStats() {
        InsightResponse response = new InsightResponse();
        response.setTotalEvents(eventRepository.count());
        response.setTotalTickets(ticketRepository.count());
        response.setTicketsSold(ticketRepository.countByPurchaserIsNotNull());
        response.setTicketsScanned(ticketRepository.countByScannedTrue());
        return response;
    }
}
