package com.smartfinance.repository;

import com.smartfinance.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
