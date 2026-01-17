package com.structify.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.structify.backend.entity.BuilderCompany;
import com.structify.backend.entity.User;
import com.structify.backend.repository.UserRepository;
import com.structify.backend.service.BuilderCompanyService;

@Controller
@RequestMapping("/builder/company")
public class BuilderCompanyController {

    private final BuilderCompanyService builderCompanyService;
    private final UserRepository userRepository;

    public BuilderCompanyController(BuilderCompanyService builderCompanyService,
                                    UserRepository userRepository) {
        this.builderCompanyService = builderCompanyService;
        this.userRepository = userRepository;
    }

    /**
     * Show company form
     */
    @GetMapping("/form")
    public String showCompanyForm(@AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {

        User builder = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow();

        // If company already exists → redirect to dashboard
        if (builderCompanyService.companyExistsForBuilder(builder)) {
            return "redirect:/builder/dashboard";
        }

        model.addAttribute("builderCompany", new BuilderCompany());
        return "company-form";
    }

    /**
     * Handle form submission
     */
    @PostMapping("/submit")
    public String submitCompanyForm(@AuthenticationPrincipal UserDetails userDetails,
                                    @ModelAttribute BuilderCompany builderCompany,
                                    Model model) {

        User builder = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow();

        try {
            builderCompanyService.saveCompany(builderCompany, builder);
        } catch (IllegalStateException ex) {
            return "redirect:/builder/dashboard";
        }

        return "redirect:/builder/dashboard";
    }
}
