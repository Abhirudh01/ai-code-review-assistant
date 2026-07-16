package com.aiAssistant.review.controller;

import com.aiAssistant.review.dto.project.PasteCodeRequest;
import com.aiAssistant.review.dto.project.ProjectResponse;
import com.aiAssistant.review.dto.project.UploadResponse;
import com.aiAssistant.review.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping(
            value = "/upload",
            consumes="multipart/form-data"
    )
    public ResponseEntity<UploadResponse>uploadProject(@RequestParam("file")MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.uploadProject(file));
    }

    @PostMapping("/paste")
    public ResponseEntity<UploadResponse> pasteCode(
            @Valid @RequestBody PasteCodeRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.pasteCode(request));
    }
    @GetMapping
    public ResponseEntity<List<ProjectResponse>>  getAllProjects(){
        return ResponseEntity.ok(projectService.getAllProjects());
    }
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

}
