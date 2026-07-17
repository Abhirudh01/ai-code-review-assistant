package com.aiAssistant.review.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer overallScore;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aiSummary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String checkstyleReport;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String pmdReport;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String spotbugsReport;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}