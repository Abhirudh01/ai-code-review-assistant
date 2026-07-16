package com.aiAssistant.review.service;

import com.aiAssistant.review.dto.project.PasteCodeRequest;
import com.aiAssistant.review.dto.project.ProjectResponse;
import com.aiAssistant.review.dto.project.UploadResponse;
import com.aiAssistant.review.entity.User;
import com.aiAssistant.review.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

    UploadResponse uploadProject(MultipartFile file);
    UploadResponse pasteCode(PasteCodeRequest request);
    List<ProjectResponse> getAllProjects();
    void deleteProject(Long projectId);
}
