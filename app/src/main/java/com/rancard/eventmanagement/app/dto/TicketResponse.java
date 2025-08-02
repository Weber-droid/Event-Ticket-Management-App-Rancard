package com.rancard.eventmanagement.app.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketResponse {
    private Long id;
    private String code;
    private boolean scanned;
    private String purchaserUsername;
    private LocalDateTime purchaseDate;
}
