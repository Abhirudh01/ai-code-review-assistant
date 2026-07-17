package com.aiAssistant.review.repository;

import com.aiAssistant.review.entity.Project;
import com.aiAssistant.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByProject(Project project);
}