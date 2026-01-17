package com.structify.backend.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class RagContextService {

    public String loadRagContext() {
        String ecoGuidelines = readFile("rag-data/builder_eco_guidelines.txt");
        String projectRules = readFile("rag-data/project_recommendation_rules.txt");

        return """
                RAG KNOWLEDGE CONTEXT:
                ---------------------
                %s

                %s
                """.formatted(ecoGuidelines, projectRules);
    }

    private String readFile(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream())
            );

            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return "RAG data not available.";
        }
    }
}
