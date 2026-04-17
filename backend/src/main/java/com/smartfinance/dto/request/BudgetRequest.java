package com.smartfinance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @JsonProperty("amount")
    private BigDecimal amount;


    @NotNull(message = "Category is required")
    private String category;
}
