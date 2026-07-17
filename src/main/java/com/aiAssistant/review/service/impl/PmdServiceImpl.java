package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.dto.AnalysisResult;
import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.service.PmdService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class PmdServiceImpl implements PmdService {

    private static final String PMD =
            "D:\\Tools\\PMD\\pmd-bin-7.26.0\\bin\\pmd.bat";

    @Override
    public AnalysisResult analyze(CodeFile codeFile) {

        try {

            File tempDir = Files.createTempDirectory("pmd").toFile();

            File temp = new File(tempDir, codeFile.getFileName());

            Files.writeString(
                    temp.toPath(),
                    codeFile.getSourceCode(),
                    StandardCharsets.UTF_8
            );

            ProcessBuilder pb = new ProcessBuilder(
                    PMD,
                    "check",
                    "-d", temp.getAbsolutePath(),
                    "-R", "category/java/bestpractices.xml",
                    "-f", "text"
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            StringBuilder report = new StringBuilder();

            String line;

            int warnings = 0;

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("[WARN]")) {
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                report.append(line).append("\n");


                if (line.contains(":")) {
                    warnings++;
                }
            }

            int exitCode = process.waitFor();

            temp.delete();
            tempDir.delete();
            String cleanedReport = report.toString()
                    .replace(tempDir.getAbsolutePath() + "\\", "");

            return AnalysisResult.builder()
                    .success(exitCode == 0 || exitCode == 4)
                    .warnings(warnings)
                    .errors(exitCode != 0 && exitCode != 4 ? 1 : 0)
                    .report(cleanedReport)
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