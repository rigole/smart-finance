package com.smartfinance.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class InsightResponse {
    private UUID id;
    private String message;
    private String type;
    private LocalDateTime generatedAt;
}
