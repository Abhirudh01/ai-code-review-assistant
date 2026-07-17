package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.dto.AnalysisResult;
import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.service.SpotBugsService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class SpotBugsServiceImpl implements SpotBugsService {

    private static final String SPOTBUGS =
            "D:\\Tools\\SpotBugs\\spotbugs-4.10.3\\bin\\spotbugs.bat";

    @Override
    public AnalysisResult analyze(CodeFile codeFile) {

        try {

            File tempDir = Files.createTempDirectory("spotbugs").toFile();

            File temp = new File(tempDir, codeFile.getFileName());

            Files.writeString(
                    temp.toPath(),
                    codeFile.getSourceCode(),
                    StandardCharsets.UTF_8
            );

            ProcessBuilder pb = new ProcessBuilder(
                    SPOTBUGS,
                    "-textui",
                    tempDir.getAbsolutePath()
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder report = new StringBuilder();
            String line;
            int warnings = 0;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                line = line.replace(tempDir.getAbsolutePath() + "\\", "");

                report.append(line).append("\n");

                warnings++;
            }

            int exitCode = process.waitFor();

            temp.delete();
            tempDir.delete();

            return AnalysisResult.builder()
                    .success(exitCode == 0)
                    .warnings(warnings)
                    .errors(exitCode != 0 ? 1 : 0)
                    .report(report.toString())
                    .build();

        } catch (Exception e) {

            return AnalysisResult.builder()
                    .success(false)
                    .warnings(0)
                    .errors(1)
                    .report(e.getMessage())
                    .build();
        }
    }
}