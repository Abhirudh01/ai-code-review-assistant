package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.service.StaticAnalysisService;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import org.springframework.stereotype.Service;

@Service
public class StaticAnalysisServiceImpl implements StaticAnalysisService {
    @Override
    public void validateJavaSyntax(CodeFile codeFile) {
        try{
            StaticJavaParser.parse(codeFile.getSourceCode());
            codeFile.setSyntaxValid(true);
            codeFile.setSyntaxErrors(null);
        }catch(ParseProblemException e){
            throw new RuntimeException("Syntax error in file:"+codeFile.getFileName(),e);
        }
    }
}
