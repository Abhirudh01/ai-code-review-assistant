package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.entity.Project;
import com.aiAssistant.review.entity.Review;
import com.aiAssistant.review.repository.ProjectRepository;
import com.aiAssistant.review.repository.ReviewRepository;
import com.aiAssistant.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ProjectRepository projectRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public void generateReview(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new RuntimeException(("Project not found")));

        Review review =Review.builder()
                .project(project)
                .overallScore(0)
                .aiSummary("Analysis pending..")
                .checkstyleReport("")
                .pmdReport("")
                .spotbugsReport("")
                .build();

        reviewRepository.save(review);
    }
}
