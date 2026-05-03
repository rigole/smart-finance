package com.smartfinance.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;



@Data
@Builder
public class InsightResponse {
    private UUID id;
    private String message;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime generatedAt;
}
