package com.structify.backend.dto;

import com.structify.backend.entity.BuilderCompany;

public class AiRecommendationResponse {

    private BuilderCompany builder;
    private String explanation;

    public AiRecommendationResponse(BuilderCompany builder, String explanation) {
        this.builder = builder;
        this.explanation = explanation;
    }

    public BuilderCompany getBuilder() {
        return builder;
    }

    public void setBuilder(BuilderCompany builder) {
        this.builder = builder;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
