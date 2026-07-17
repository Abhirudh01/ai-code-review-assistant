package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.repository.CodeFileRepository;
import com.aiAssistant.review.service.StaticAnalysisService;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StaticAnalysisServiceImpl implements StaticAnalysisService {
    private final CodeFileRepository codeFileRepository;
    @Override
    public void validateJavaSyntax(CodeFile codeFile) {
        try{
            StaticJavaParser.parse(codeFile.getSourceCode());
            codeFile.setSyntaxValid(true);
            codeFile.setSyntaxErrors(null);
        }catch(ParseProblemException e){
            codeFile.setSyntaxValid(false);
            throw new RuntimeException("Syntax error in file:"+codeFile.getFileName(),e);
        }
        codeFileRepository.save(codeFile);

    }
}
