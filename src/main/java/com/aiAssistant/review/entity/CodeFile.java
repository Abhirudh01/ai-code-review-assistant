package com.aiAssistant.review.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgrammingLanguage language;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceCode;

    @Column(nullable = false)
    @Builder.Default
    private boolean syntaxValid = true;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String syntaxErrors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}