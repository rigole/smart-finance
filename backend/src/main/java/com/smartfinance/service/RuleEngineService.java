package com.smartfinance.service;

import com.smartfinance.model.Budget;
import com.smartfinance.model.Insight;
import com.smartfinance.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RuleEngineService {
    public List<String> analyze(List<Transaction> transactions, List<Budget> budgets) {
        List<String> insights = new ArrayList<>();
        insights.addAll(analyzeSpending(transactions));
        insights.addAll(analyzeBudgets(budgets, transactions));
        insights.addAll(analyzeSavings(transactions));
        return insights;
    }

    private List<String> analyzeSavings(List<Transaction> transactions) {
        List<String> insights = new ArrayList<>();
        LocalDate now = LocalDate.now();

        BigDecimal income = transactions.stream()
                .filter(t ->t.getType().equals("INCOME")
                        && t.getDate().getMonth() == now.getMonth()
                        && t.getDate().getYear() == now.getYear())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        BigDecimal expenses = transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE")
                        && t.getDate().getMonth() == now.getMonth()
                        && t.getDate().getYear() == now.getYear())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (income.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal savingsRate = income.subtract(expenses)
                    .divide(income, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            insights.add(String.format("SAVINGS_RATE: %.0f", savingsRate));
        }
        return insights;
    }

    private List<String> analyzeBudgets(List<Budget> budgets, List<Transaction> transactions) {
        List<String> insights = new ArrayList<>();

        for (Budget budget : budgets) {
            BigDecimal spent = budget.getSpent() != null
                    ? budget.getSpent()
                    : BigDecimal.ZERO;
            BigDecimal limit  = budget.getAmount();

            if (limit.compareTo(BigDecimal.ZERO) == 0) continue;

            double percentage = spent.doubleValue() / limit.doubleValue() * 100;
            if (percentage >= 100) {
                insights.add("BUDGET_EXCEEDED:" + budget.getCategory() + ":" + String.format("%.0f", percentage) + "%" );
            } else if (percentage >= 75) {
                insights.add("BUDGET_WARNING:" + budget.getCategory() + ":" + String.format("%.0f", percentage) + "%" );
            }
        }
        return insights;
    }

    private List<String> analyzeSpending(List<Transaction> transactions) {
        List<String> insights = new ArrayList<>();
        LocalDate now = LocalDate.now();

        BigDecimal currentMonthExpenses = transactions.stream()
          .filter(t -> t.getType().equals("EXPENSE")
            && t.getDate().getMonth() == now.getMonth()
            && t.getDate().getYear() == now.getYear())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        BigDecimal lastMonthExpenses = transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE")
                && t.getDate().getMonth()
                        == now.minusMonths(1).getMonth()
                && t.getDate().getYear()
                        == now.minusMonths(1).getYear())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        if (lastMonthExpenses.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal change = currentMonthExpenses
                    .subtract(lastMonthExpenses)
                    .divide(lastMonthExpenses, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            if (change.compareTo(BigDecimal.ZERO) > 0) {
                insights.add(String.format("SPENDING_INCREASE:%.0f", change));
            } else if (change.compareTo(BigDecimal.valueOf(-10)) < 0) {
                insights.add(String.format("SPENDING_DECREASE:%.0f", change.abs()));
            }
        }
        Map<String, BigDecimal> byCategory = transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE")
                        && t.getCategory() != null)
                .collect(Collectors.groupingBy(transaction -> transaction.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));


        byCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> insights.add(
                        "TOP_CATEGORY:" + entry.getKey() + ":" + entry.getValue()));

        return insights;

    }

}
