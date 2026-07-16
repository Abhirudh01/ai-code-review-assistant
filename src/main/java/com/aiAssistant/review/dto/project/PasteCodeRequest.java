package com.aiAssistant.review.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasteCodeRequest {

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Code cannot be empty")
    private String code;
}