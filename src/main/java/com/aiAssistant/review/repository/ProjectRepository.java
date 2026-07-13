package com.aiAssistant.review.repository;

import com.aiAssistant.review.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}