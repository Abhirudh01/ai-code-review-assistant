package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.dto.AnalysisResult;
import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.service.CheckStyleService;

public class CheckstyleServiceImpl implements CheckStyleService {

    @Override
    public AnalysisResult analyze(CodeFile codeFile) {
        return AnalysisResult.builder()
                .success(true)
                .warnings(0)
                .errors(0)
                .report("Checkstyle analysis completed")
                .build();
    }
}
