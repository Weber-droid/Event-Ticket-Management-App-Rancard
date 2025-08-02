package com.rancard.eventmanagement.app.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String ownerUsername;
    private List<TicketResponse> tickets;
}

