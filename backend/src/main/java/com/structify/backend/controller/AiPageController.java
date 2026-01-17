package com.structify.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AiPageController {

    @GetMapping("/ai/recommend-page")
    public String aiRecommendPage() {
        return "ai-recommendation";
    }
}
