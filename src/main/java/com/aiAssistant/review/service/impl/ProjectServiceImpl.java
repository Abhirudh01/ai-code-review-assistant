package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.dto.project.PasteCodeRequest;
import com.aiAssistant.review.dto.project.ProjectResponse;
import com.aiAssistant.review.dto.project.UploadResponse;
import com.aiAssistant.review.entity.Project;
import com.aiAssistant.review.entity.ProjectStatus;
import com.aiAssistant.review.entity.User;
import com.aiAssistant.review.repository.ProjectRepository;
import com.aiAssistant.review.repository.UserRepository;
import com.aiAssistant.review.service.ProjectService;
import com.aiAssistant.review.service.ZipExtractionService;
import com.aiAssistant.review.validation.CodeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ZipExtractionService zipExtractionService;

    private User getCurrentUser(){
        Authentication authentication=
                SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException("User Not found"));
    }
    @Override
    public UploadResponse uploadProject(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("ZIP file is empty.");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.toLowerCase().endsWith(".zip")) {
            throw new RuntimeException("Only ZIP files are allowed.");
        }
        User user=getCurrentUser();
        Project project= Project.builder()
                .name(file.getOriginalFilename())
                .status(ProjectStatus.UPLOADED)
                .uploadedAt(LocalDateTime.now())
                .user(user)
                .build();
        projectRepository.save(project);
        zipExtractionService.extractAndSaveFiles(file, project);
        return UploadResponse.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .status(project.getStatus().name())
                .build();

    }

    @Override
    public UploadResponse pasteCode(PasteCodeRequest request) {
        CodeValidator.validateJavaCode(request.getCode());
        User user = getCurrentUser();
        Project project=Project.builder()
                .name(request.getProjectName())
                .status(ProjectStatus.UPLOADED)
                .uploadedAt(LocalDateTime.now())
                .user(user)
                .build();
        projectRepository.save(project);

        return UploadResponse.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .status(project.getStatus().name())
                .build();
    }


    @Override
    public List<ProjectResponse> getAllProjects() {
        User user=getCurrentUser();
        List<Project> projects=projectRepository.findByUser(user);
        return projects.stream()
                .map(project-> ProjectResponse.builder()
                        .id(project.getId())
                        .projectName(project.getName())
                        .status(project.getStatus())
                        .uploadedAt(project.getUploadedAt())
                        .build())
                .toList();
    }

    @Override
    public void deleteProject(Long projectId) {
        User user=getCurrentUser();
        Project project=projectRepository.findById(projectId)
                .orElseThrow(()->new RuntimeException("Project Not Found"));

        if(!project.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        projectRepository.delete(project);

    }
}
