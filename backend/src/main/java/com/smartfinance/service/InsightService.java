package com.smartfinance.service;

import com.smartfinance.model.Insight;
import com.smartfinance.model.User;
import com.smartfinance.repository.BudgetRepository;
import com.smartfinance.repository.InsightRepository;
import com.smartfinance.repository.TransactionRepository;
import com.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.smartfinance.dto.response.InsightResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightService {
    private GroqService  groqService;
    private TransactionRepository transactionRepository;
    private RuleEngineService ruleEngineService;
    private BudgetRepository  budgetRepository;
    private UserRepository userRepository;
    private InsightRepository insightRepository;


    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    public List<InsightResponse> generateInsights(){
        User user = getCurrentUser();

        var transactions = transactionRepository.findByUserIdOrderByDateDesc(user.getId());

        var budgets = budgetRepository.findByUserId(user.getId());

        List<String> rawInsights = ruleEngineService.analyze(transactions, budgets);

        insightRepository.deleteByUserId(user.getId());

        List<Insight> insights = rawInsights.stream().map(raw -> {
            String message = groqService.generateInsight(raw);
            return Insight.builder()
                    .user(user)
                    .message(message)
                    .type(raw.split(":")[0])
                    .build();
        }).collect(Collectors.toList());
        insightRepository.saveAll(insights);
        
        return insights.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InsightResponse> getInsights(){
        User user = getCurrentUser();
        return insightRepository.findByUserIdOrderByGeneratedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InsightResponse mapToResponse(Insight insight) {
        return InsightResponse.builder()
                .id(insight.getId())
                .message(insight.getMessage())
                .type(insight.getType())
                .generatedAt(insight.getGeneratedAt())
                .build();
    }
}
