package com.aiAssistant.review.repository;

import com.aiAssistant.review.entity.Project;
import com.aiAssistant.review.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
}