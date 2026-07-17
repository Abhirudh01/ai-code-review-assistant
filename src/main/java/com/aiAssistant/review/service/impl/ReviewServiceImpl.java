package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.dto.AnalysisResult;
import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.entity.Project;
import com.aiAssistant.review.entity.Review;
import com.aiAssistant.review.repository.ProjectRepository;
import com.aiAssistant.review.repository.ReviewRepository;
import com.aiAssistant.review.service.CheckStyleService;
import com.aiAssistant.review.service.PmdService;
import com.aiAssistant.review.service.ReviewService;
import com.aiAssistant.review.service.SpotBugsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ProjectRepository projectRepository;
    private final ReviewRepository reviewRepository;
    private final CheckStyleService checkStyleService;
    private final PmdService pmdService;
    private final SpotBugsService spotBugsService;

    @Override
    public void generateReview(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        StringBuilder checkstyleReport = new StringBuilder();
        StringBuilder pmdReport = new StringBuilder();
        StringBuilder spotbugsReport = new StringBuilder();

        int totalWarnings = 0;
        int totalErrors = 0;

        for (CodeFile codeFile : project.getCodeFiles()) {

            AnalysisResult checkstyleResult = checkStyleService.analyze(codeFile);
            AnalysisResult pmdResult = pmdService.analyze(codeFile);
            AnalysisResult spotbugsResult = spotBugsService.analyze(codeFile);

            checkstyleReport.append("File: ")
                    .append(codeFile.getFileName())
                    .append("\n")
                    .append(checkstyleResult.getReport())
                    .append("\n\n");

            pmdReport.append("File: ")
                    .append(codeFile.getFileName())
                    .append("\n")
                    .append(pmdResult.getReport())
                    .append("\n\n");
            spotbugsReport.append("File: ")
                    .append(codeFile.getFileName())
                    .append("\n")
                    .append(spotbugsResult.getReport())
                    .append("\n\n");

            totalWarnings += checkstyleResult.getWarnings()
                    + pmdResult.getWarnings()
                    + spotbugsResult.getWarnings();

            totalErrors += checkstyleResult.getErrors()
                    + pmdResult.getErrors()
                    + spotbugsResult.getErrors();
        }
        int score = Math.max(100 - (totalWarnings * 2) - (totalErrors * 10), 0);

        String summary = """
            Analysis completed successfully.

            Total Warnings: %d
            Total Errors: %d

            Overall code quality is %s.
            """.formatted(
                totalWarnings,
                totalErrors,
                score >= 80 ? "Good" : "Needs Improvement"
        );

        Review review = Review.builder()
                .project(project)
                .overallScore(score)
                .aiSummary(summary)
                .checkstyleReport(checkstyleReport.toString())
                .pmdReport(pmdReport.toString())
                .spotbugsReport(spotbugsReport.toString())
                .build();

        reviewRepository.save(review);
    }
}
