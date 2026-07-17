package com.aiAssistant.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResult {
    private boolean success;
    private int warnings;
    private int errors;
    private String report;
}
