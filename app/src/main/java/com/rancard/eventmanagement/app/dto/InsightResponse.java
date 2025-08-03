package com.rancard.eventmanagement.app.dto;

import lombok.Data;

@Data
public class InsightResponse {
    private long totalEvents;
    private long totalTickets;
    private long ticketsSold;
    private long ticketsScanned;
}
