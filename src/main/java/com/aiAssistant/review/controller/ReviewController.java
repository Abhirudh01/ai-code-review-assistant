package com.aiAssistant.review.controller;

import com.aiAssistant.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{projectId}")
    public ResponseEntity<String> generateReview(
            @PathVariable Long projectId){
        reviewService.generateReview(projectId);
        return ResponseEntity.ok("Review generated successfully.");
    }
}
