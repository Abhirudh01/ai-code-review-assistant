package com.aiAssistant.review.service;

import com.aiAssistant.review.dto.AnalysisResult;
import com.aiAssistant.review.entity.CodeFile;

public interface PmdService {
    AnalysisResult analyze(CodeFile codeFile);
}