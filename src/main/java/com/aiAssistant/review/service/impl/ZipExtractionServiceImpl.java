package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.entity.ProgrammingLanguage;
import com.aiAssistant.review.entity.Project;
import com.aiAssistant.review.repository.CodeFileRepository;
import com.aiAssistant.review.service.StaticAnalysisService;
import com.aiAssistant.review.service.ZipExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class ZipExtractionServiceImpl implements ZipExtractionService {
    private final CodeFileRepository codeFileRepository;
    private final StaticAnalysisService staticAnalysisService;
    @Override
    public void extractAndSaveFiles(MultipartFile zipFile, Project project) {
        try(ZipInputStream zis=new ZipInputStream(zipFile.getInputStream())){

            ZipEntry entry;
            while((entry=zis.getNextEntry())!=null){
                if(entry.isDirectory()){
                    zis.closeEntry();
                    continue;
                }
                if(!entry.getName().toLowerCase().endsWith(".java")){
                    zis.closeEntry();
                    continue;
                }
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                byte[]buffer=new byte[4096];
                int length;
                while((length=zis.read(buffer))!=-1){
                    baos.write(buffer,0,length);
                }

                String sourceCode=baos.toString(StandardCharsets.UTF_8);
                CodeFile codeFile =CodeFile.builder()
                        .fileName(entry.getName())
                        .language(ProgrammingLanguage.JAVA)
                        .sourceCode(sourceCode)
                        .project(project)
                        .build();
                codeFileRepository.save(codeFile);
                staticAnalysisService.validateJavaSyntax(codeFile);
                zis.closeEntry();

            }
        }catch(IOException e){
            throw new RuntimeException("Unable to extract ZIP",e);
        }

    }
}
