package com.structify.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.structify.backend.service.BuilderCompanyService;

@Controller
public class BuilderViewController {

    private final BuilderCompanyService builderCompanyService;

    public BuilderViewController(BuilderCompanyService builderCompanyService) {
        this.builderCompanyService = builderCompanyService;
    }

    @GetMapping("/builders")
    public String viewBuilders(Model model) {
        model.addAttribute(
            "builders",
            builderCompanyService.getAllApprovedBuilders()
        );
        return "builders";
    }
}

