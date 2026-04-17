package com.smartfinance.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class BudgetResponse {
    private UUID id;
    private BigDecimal amount;
    private String category;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
}
