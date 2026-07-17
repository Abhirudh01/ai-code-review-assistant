package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.dto.AnalysisResult;
import com.aiAssistant.review.entity.CodeFile;
import com.aiAssistant.review.service.CheckStyleService;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;

@Service
public class CheckstyleServiceImpl implements CheckStyleService {

    @Override
    public AnalysisResult analyze(CodeFile codeFile) {

        try {

            File temp = File.createTempFile("code", ".java");

            try (FileWriter writer = new FileWriter(temp)) {
                writer.write(codeFile.getSourceCode());
            }

            Configuration configuration =
                    ConfigurationLoader.loadConfiguration(
                            String.valueOf(new ClassPathResource("checkstyle.xml").getURL()),
                            new PropertiesExpander(System.getProperties())
                    );

            Checker checker = new Checker();

            StringBuilder report = new StringBuilder();

            final int[] warnings = {0};

            checker.setModuleClassLoader(Checker.class.getClassLoader());

            checker.addListener(new AuditListener() {

                @Override
                public void addError(AuditEvent event) {
                    warnings[0]++;
                    report.append("Line ")
                            .append(event.getLine())
                            .append(": ")
                            .append(event.getMessage())
                            .append("\n");
                }

                @Override public void auditStarted(AuditEvent event) {}
                @Override public void auditFinished(AuditEvent event) {}
                @Override public void fileStarted(AuditEvent event) {}
                @Override public void fileFinished(AuditEvent event) {}
                @Override public void addException(AuditEvent event, Throwable throwable) {}
            });

            checker.configure(configuration);

            checker.process(Collections.singletonList(temp));

            checker.destroy();

            temp.delete();

            return AnalysisResult.builder()
                    .success(true)
                    .warnings(warnings[0])
                    .errors(0)
                    .report(
                            warnings[0] == 0 ?
                                    "No Checkstyle violations found."
                                    :
                                    report.toString()
                    )
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