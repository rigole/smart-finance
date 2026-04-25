package com.smartfinance.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@AllArgsConstructor
@Setter
@Getter
@Table(name = "insights")
@NoArgsConstructor
@Builder
public class Insight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false)
    private String message;

    private String type;

    @Builder.Default
    @Column(name = "generated_at")
    private LocalDateTime generatedAt =  LocalDateTime.now();
}
