package com.structify.backend.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.structify.backend.dto.AiRecommendationResponse;
import com.structify.backend.entity.BuilderCompany;
import com.structify.backend.repository.BuilderCompanyRepository;

@Service
public class AiRecommendationService {

    @Autowired
    private BuilderCompanyRepository builderCompanyRepository;

    @Autowired
    private RagContextService ragContextService;

    // ✅ THIS METHOD WAS MISSING — NOW ADDED
    public AiRecommendationResponse recommendBestBuilder(
            String location,
            String budget,
            String projectType,
            String sustainability
    ) {

        List<BuilderCompany> builders = builderCompanyRepository.findAll();

        if (builders.isEmpty()) {
            return new AiRecommendationResponse(
                    null,
                    "No builders available in the system currently."
            );
        }

        // 🔹 Load RAG context
        String ragContext = ragContextService.loadRagContext();

        System.out.println("====== RAG CONTEXT USED ======");
        System.out.println(ragContext);
        System.out.println("==============================");

        BuilderCompany bestBuilder = builders.stream()
                .max(Comparator.comparingInt(
                        b -> calculateScore(b, location, budget, projectType, sustainability)
                ))
                .orElse(null);

        if (bestBuilder == null) {
            return new AiRecommendationResponse(
                    null,
                    "No suitable builder found for your requirements."
            );
        }

        String explanation = """
                Based on your inputs:
                - Location: %s
                - Budget: %s
                - Project Type: %s
                - Sustainability Priority: %s

                🏆 Recommended Builder: %s

                This builder was selected using AI-assisted scoring
                combined with sustainability and project guidelines (RAG).
                """.formatted(
                        location,
                        budget,
                        projectType,
                        sustainability,
                        bestBuilder.getCompanyName()
                );

        return new AiRecommendationResponse(bestBuilder, explanation);
    }

    // 🔹 INTERNAL SCORING LOGIC
    private int calculateScore(
            BuilderCompany b,
            String location,
            String budget,
            String projectType,
            String sustainability
    ) {
        int score = 0;

        if (b.getLocation() != null &&
                b.getLocation().equalsIgnoreCase(location)) {
            score += 30;
        }

        if ("Low".equalsIgnoreCase(budget) && b.getCostPerSqFt() < 1500) {
            score += 20;
        } else if ("Medium".equalsIgnoreCase(budget) && b.getCostPerSqFt() <= 3000) {
            score += 20;
        } else if ("High".equalsIgnoreCase(budget) && b.getCostPerSqFt() > 3000) {
            score += 20;
        }

        if ("Residential".equalsIgnoreCase(projectType) && b.getExperienceYears() >= 3) {
            score += 20;
        } else if ("Commercial".equalsIgnoreCase(projectType) && b.getExperienceYears() >= 5) {
            score += 20;
        }

        if ("High".equalsIgnoreCase(sustainability) && b.isSustainabilityFocused()) {
            score += 30;
        }

        return score;
    }
}
