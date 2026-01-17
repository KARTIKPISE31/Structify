package com.structify.backend.dto;

public class AiBuilderResponse {

    private String companyName;
    private String location;
    private int score;

    public AiBuilderResponse(String companyName, String location, int score) {
        this.companyName = companyName;
        this.location = location;
        this.score = score;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public int getScore() {
        return score;
    }
}
