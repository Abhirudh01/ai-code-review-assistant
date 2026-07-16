package com.aiAssistant.review.dto.project;

import com.aiAssistant.review.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String projectName;
    private ProjectStatus status;
    private LocalDateTime uploadedAt;
}
