package com.structify.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.structify.backend.repository.UserRepository;

@Service
public class GraniteAiService {

    @Autowired
    private UserRepository userRepository;

    // In-memory vector store
    private final Map<String, List<Double>> vectorStore = new HashMap<>();

    // Generate builder documents
    public List<String> generateBuilderDocs() {
        return userRepository.findByRoleAndApproved("ROLE_BUILDER", true)
                .stream()
                .map(b -> "Builder Name: " + b.getName()
                        + ", Experience: " + b.getExperience() + " years"
                        + ", Skills: " + b.getSkills()
                        + ", Location: " + b.getLocation())
                .collect(Collectors.toList());
    }

    // Mock embeddings (for testing without IBM API key)
    public List<Double> embedText(String text) {
        List<Double> vector = new ArrayList<>();
        int hash = text.hashCode();
        Random random = new Random(hash);
        for (int i = 0; i < 128; i++) { // 128-dim vector
            vector.add(random.nextDouble());
        }
        return vector;
    }

    // Store builder embeddings
    public void ingestBuilders() {
        generateBuilderDocs().forEach(doc ->
                vectorStore.put(doc, embedText(doc))
        );
    }

    // Search top 3 similar builders
    public List<String> recommendBuilders(String query) {
        List<Double> queryVector = embedText(query);
        return vectorStore.entrySet()
                .stream()
                .sorted((a, b) ->
                        Double.compare(cosineSimilarity(b.getValue(), queryVector),
                                cosineSimilarity(a.getValue(), queryVector)))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

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
