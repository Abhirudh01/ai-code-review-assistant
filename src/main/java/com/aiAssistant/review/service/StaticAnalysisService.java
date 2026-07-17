package com.aiAssistant.review.service;

import com.aiAssistant.review.entity.CodeFile;

public interface StaticAnalysisService {
    void validateJavaSyntax(CodeFile codeFile);
}
