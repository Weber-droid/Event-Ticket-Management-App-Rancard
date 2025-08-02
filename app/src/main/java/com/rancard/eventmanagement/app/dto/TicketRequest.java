package com.rancard.eventmanagement.app.dto;

import lombok.Data;

@Data
public class TicketRequest {
    private String code;  // you can use UUID or let server generate it
}

