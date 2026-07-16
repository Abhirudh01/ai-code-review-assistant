package com.aiAssistant.review.repository;

import com.aiAssistant.review.entity.CodeFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeFileRepository extends JpaRepository<CodeFile,Long> {
}
