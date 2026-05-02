package com.smartfinance.controller;

import com.smartfinance.dto.response.InsightResponse;
import com.smartfinance.model.Insight;
import com.smartfinance.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/insights")
@CrossOrigin(origins = "http://localhost:4200")
public class InsightController {

    private final InsightService insightService;

    @GetMapping
    public ResponseEntity<List<InsightResponse>> getInsights(){
        return ResponseEntity.ok(insightService.getInsights());
    }

    @PostMapping("/generate")
    public ResponseEntity<List<InsightResponse>> generateInsights(){
        return ResponseEntity.ok(insightService.generateInsights());
    }

}
