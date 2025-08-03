package com.rancard.eventmanagement.app.controller;

import com.rancard.eventmanagement.app.dto.InsightResponse;

import com.rancard.eventmanagement.app.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;

    @GetMapping("/insights")
    public ResponseEntity<InsightResponse> getInsights() {
        return ResponseEntity.ok(insightService.getStats());
    }
}
