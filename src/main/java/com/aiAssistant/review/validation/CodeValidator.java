package com.aiAssistant.review.validation;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;

public class CodeValidator {
    public static void validateJavaCode(String code){
        try{
            StaticJavaParser.parse(code);
        }
        catch(ParseProblemException e){
            throw new RuntimeException("Invalid Java source code");
        }
    }
}
