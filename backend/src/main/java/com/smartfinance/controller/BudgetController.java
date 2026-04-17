package com.smartfinance.controller;

import com.smartfinance.dto.request.BudgetRequest;
import com.smartfinance.dto.request.TransactionRequest;
import com.smartfinance.dto.response.BudgetResponse;
import com.smartfinance.dto.response.TransactionResponse;
import com.smartfinance.model.User;
import com.smartfinance.service.BudgetService;
import com.smartfinance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BudgetController {

    private final BudgetService budgetService;


    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(budgetService.getAllBudgets(user));
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> create(
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(
                budgetService.createBudget(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody BudgetRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(budgetService.updateBudget(id,request,user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @AuthenticationPrincipal User user){
        budgetService.deleteBudget(id,user);
        return ResponseEntity.noContent().build();
    }



}
