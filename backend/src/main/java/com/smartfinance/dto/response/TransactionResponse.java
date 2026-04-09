package com.smartfinance.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionResponse {
    private UUID id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private String type;
    private String categoryName;
    private LocalDateTime createdAt;
}
