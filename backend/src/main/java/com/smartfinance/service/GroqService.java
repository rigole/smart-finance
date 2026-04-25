package com.smartfinance.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String model;

    private final RestTemplate restTemplate;

    public String generateInsight(String rawInsight) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            String prompt = buildPromt(rawInsight);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", List.of(message));
            body.put("max_tokens", 100);
            body.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode() ==  HttpStatus.OK && response.getBody() != null) {
                List<Map> choices = (List<Map>) response.getBody().get("choices");
                if (choices != null && choices.isEmpty()) {
                    Map messageResponse = (Map) choices.get(0).get("message");
                    return (String) messageResponse.get("content");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return fallbackMessage(rawInsight);
        }
        return fallbackMessage(rawInsight);
    }

    private String fallbackMessage(String raw) {
        if (raw.startsWith("SPENDING_INCREASE"))
            return "Your spending increased this month. Consider reviewing your expenses!";
        if (raw.startsWith("SPENDING_DECREASE"))
            return "Great job! Your spending decreased this month!";
        if (raw.startsWith("BUDGET_EXCEEDED"))
            return "You have exceeded a budget limit. Time to review!";
        if (raw.startsWith("BUDGET_WARNING"))
            return "One of your budgets is almost full!";
        if (raw.startsWith("SAVINGS_RATE"))
            return "Keep tracking your savings to reach your goals!";
        if (raw.startsWith("TOP_CATEGORY"))
            return "You have a top spending category this month!";
        return "Keep tracking your finances for better insights!";
    }

    private String buildPromt(String rawInsight) {
        return """
            You are a friendly personal finance assistant.
            Convert this raw financial data into one short,
            friendly and actionable insight in 1-2 sentences.
            Use emojis. Be encouraging but honest.
            
            Raw data: %s
            
            Respond with just the insight message, nothing else.
            """.formatted(rawInsight);
    }
}
