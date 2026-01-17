package com.structify.backend.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.structify.backend.entity.BuilderCompany;
import com.structify.backend.repository.BuilderCompanyRepository;

@Controller
public class BuilderFilterController {

    private final BuilderCompanyRepository repository;

    public BuilderFilterController(BuilderCompanyRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/builders/filter")
    public String filterBuilders(
            @RequestParam(required = false) Double maxCost,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) boolean sustainable,
            Model model) {

        List<BuilderCompany> results =
                repository.filterBuilders(maxCost, location, sustainable);

        model.addAttribute("builders", results);
        return "builders";
    }
}
