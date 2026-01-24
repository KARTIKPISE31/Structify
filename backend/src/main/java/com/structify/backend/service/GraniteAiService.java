package com.structify.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.structify.backend.entity.BuilderCompany;

@Service
public class GraniteAiService {

    // In-memory vector store
    private final Map<BuilderCompany, List<Double>> vectorStore = new HashMap<>();

    /* ------------------------------------------------
     * 1️⃣ INGEST BUILDERS (FIXED)
     * ------------------------------------------------ */
    public void ingestBuilders(List<BuilderCompany> builders) {
        vectorStore.clear();

        for (BuilderCompany builder : builders) {
            String doc = buildBuilderDocument(builder);
            vectorStore.put(builder, embedText(doc));
        }
    }

    /* ------------------------------------------------
     * 2️⃣ RECOMMEND BEST BUILDER (NEW)
     * ------------------------------------------------ */
    public BuilderCompany recommendBestBuilder(String query) {

        if (vectorStore.isEmpty()) {
            return null;
        }

        List<Double> queryVector = embedText(query);

        BuilderCompany bestBuilder = null;
        double bestScore = -1;

        for (Map.Entry<BuilderCompany, List<Double>> entry : vectorStore.entrySet()) {
            double similarity = cosineSimilarity(queryVector, entry.getValue());

            if (similarity > bestScore) {
                bestScore = similarity;
                bestBuilder = entry.getKey();
            }
        }

        return bestBuilder;
    }

    /* ------------------------------------------------
     * 3️⃣ BUILD DOCUMENT (RAG INPUT)
     * ------------------------------------------------ */
    private String buildBuilderDocument(BuilderCompany b) {
        return """
                Builder Name: %s
                Location: %s
                Experience: %d years
                Cost per SqFt: %.2f
                Sustainability Focused: %s
                """
                .formatted(
                        b.getCompanyName(),
                        b.getLocation(),
                        b.getExperienceYears(),
                        b.getCostPerSqFt(),
                        b.isSustainabilityFocused() ? "Yes" : "No"
                );
    }

    /* ------------------------------------------------
     * 4️⃣ MOCK EMBEDDINGS (SAFE FOR INTERNSHIP)
     * ------------------------------------------------ */
    private List<Double> embedText(String text) {
        List<Double> vector = new ArrayList<>();
        Random random = new Random(text.hashCode());

        for (int i = 0; i < 128; i++) {
            vector.add(random.nextDouble());
        }
        return vector;
    }

    /* ------------------------------------------------
     * 5️⃣ COSINE SIMILARITY
     * ------------------------------------------------ */
    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        double dot = 0, normA = 0, normB = 0;

        for (int i = 0; i < v1.size(); i++) {
            dot += v1.get(i) * v2.get(i);
            normA += v1.get(i) * v1.get(i);
            normB += v2.get(i) * v2.get(i);
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
