package com.aiAssistant.review.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UploadResponse {
    private Long projectId;
    private String projectName;
    private String status;
}
