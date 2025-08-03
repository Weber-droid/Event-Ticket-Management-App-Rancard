package com.rancard.eventmanagement.app.controller;

import com.rancard.eventmanagement.app.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping("/events/subscribe")
    public SseEmitter subscribeToEvents() {
        return sseService.subscribe();
    }
}
