package com.structify.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.structify.backend.dto.AiRecommendationResponse;   // ✅ REQUIRED
import com.structify.backend.service.AiRecommendationService;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiRecommendationService aiRecommendationService;

    @PostMapping("/recommend-from-db")
    public AiRecommendationResponse recommendBestBuilder(
            @RequestBody Map<String, String> input
    ) {

        return aiRecommendationService.recommendBestBuilder(
                input.getOrDefault("location", ""),
                input.getOrDefault("budget", ""),
                input.getOrDefault("projectType", ""),
                input.getOrDefault("sustainability", "")
        );
    }
}
