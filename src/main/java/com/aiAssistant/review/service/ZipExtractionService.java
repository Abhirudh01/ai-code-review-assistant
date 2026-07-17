package com.aiAssistant.review.service;

import com.aiAssistant.review.entity.Project;
import org.springframework.web.multipart.MultipartFile;

public interface ZipExtractionService {
    void extractAndSaveFiles(MultipartFile zipFile, Project project);
}
