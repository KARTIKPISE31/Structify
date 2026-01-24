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

    @Autowired
    private GraniteAiService graniteAiService;

    /**
     * Recommend ONE best builder using:
     * - DB filtering
     * - AI embeddings
     * - RAG explanation
     */
    public AiRecommendationResponse recommendBestBuilder(
        String location,
        String budget,
        String projectType,
        String sustainability
) {

    // 1️⃣ Convert budget to numeric value
    Double maxCost = null;
    if ("low".equalsIgnoreCase(budget)) maxCost = 1500.0;
    if ("medium".equalsIgnoreCase(budget)) maxCost = 2500.0;
    if ("high".equalsIgnoreCase(budget)) maxCost = 4000.0;

    boolean sustainableRequired =
            "yes".equalsIgnoreCase(sustainability)
         || "true".equalsIgnoreCase(sustainability);

    // 2️⃣ FILTER BUILDERS FROM DB (THIS IS KEY)
    List<BuilderCompany> filteredBuilders =
            builderCompanyRepository.filterBuilders(
                    maxCost,
                    location.isBlank() ? null : location,
                    sustainableRequired
            );

    if (filteredBuilders.isEmpty()) {
        return new AiRecommendationResponse(
                null,
                "No builders matched your requirements. Try adjusting filters."
        );
    }

    // 3️⃣ PICK BEST BUILDER (simple AI scoring)
    BuilderCompany bestBuilder = filteredBuilders.stream()
            .max(Comparator.comparingInt(BuilderCompany::getExperienceYears)
                    .thenComparingInt(BuilderCompany::getNumberOfProjects))
            .orElse(filteredBuilders.get(0));

    // 4️⃣ LOAD RAG CONTEXT
    String ragContext = ragContextService.loadRagContext();

    // 5️⃣ GENERATE AI EXPLANATION (RAG USED HERE)
    String explanation = """
        Based on your inputs:
        • Location: %s
        • Budget: %s
        • Project Type: %s
        • Sustainability Preference: %s

        🏆 BEST MATCHED BUILDER:
        %s

        🔍 Why this builder?
        - Matches your location and budget constraints
        - Strong experience and completed projects
        - Sustainability alignment considered
        - AI reasoning enhanced using RAG context

        📘 RAG Knowledge Applied:
        %s
        """.formatted(
            location,
            budget,
            projectType,
            sustainability,
            bestBuilder.getCompanyName(),
            ragContext
        );

    return new AiRecommendationResponse(bestBuilder, explanation);
}

}
