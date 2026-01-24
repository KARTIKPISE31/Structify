package com.structify.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.structify.backend.dto.AiRecommendationResponse;
import com.structify.backend.service.AiRecommendationService;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiRecommendationService aiRecommendationService;

    // ✅ API used by fetch() from Thymeleaf page
    @PostMapping("/recommend-from-db")
    public ResponseEntity<AiRecommendationResponse> recommendBestBuilder(
            @RequestBody Map<String, String> input
    ) {
        try {
            AiRecommendationResponse response =
                    aiRecommendationService.recommendBestBuilder(
                            input.getOrDefault("location", ""),
                            input.getOrDefault("budget", ""),
                            input.getOrDefault("projectType", ""),
                            input.getOrDefault("sustainability", "")
                    );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // 🔴 VERY IMPORTANT for debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
