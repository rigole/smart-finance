package com.smartfinance.service;

import com.smartfinance.dto.request.BudgetRequest;
import com.smartfinance.dto.request.TransactionRequest;
import com.smartfinance.dto.response.BudgetResponse;
import com.smartfinance.dto.response.TransactionResponse;
import com.smartfinance.model.Budget;
import com.smartfinance.model.Transaction;
import com.smartfinance.model.User;
import com.smartfinance.repository.BudgetRepository;
import com.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class BudgetService {
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;


    public List<BudgetResponse> getAllBudgets(User user) {
        return budgetRepository.findByUserIdOrderByCreatedAtDesc
                        (user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BudgetResponse createBudget(BudgetRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User managedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = Budget.builder()
                .amount(request.getAmount())
                .category(request.getCategory())
                .user(managedUser)
                .build();
        return mapToResponse(budgetRepository.save(budget));
    }

    public BudgetResponse updateBudget(UUID id, BudgetRequest request, User user) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User not the same user");
        }
        budget.setCategory(request.getCategory());
        budget.setAmount(request.getAmount());
        budget.setUser(user);
        return mapToResponse(budgetRepository.save(budget));
    }

    public void deleteBudget(UUID id, User user) {
        Budget budget = budgetRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        budgetRepository.delete(budget);
    }

    private BudgetResponse mapToResponse(Budget b) {
        return BudgetResponse.builder()
                .id(b.getId())
                .amount(b.getAmount())
                .category(b.getCategory())
                .createdAt(b.getCreatedAt())
                .build();
    }

}
