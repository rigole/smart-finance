package com.smartfinance.repository;


import com.smartfinance.model.Insight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InsightRepository extends JpaRepository<Insight, UUID> {


    List<Insight> findByUserIdOrderByGeneratedAtDesc(UUID userId);
    void deleteByUserId(UUID userId);
}
