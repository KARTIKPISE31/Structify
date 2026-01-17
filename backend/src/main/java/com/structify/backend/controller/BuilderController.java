package com.structify.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.structify.backend.entity.User;
import com.structify.backend.repository.UserRepository;

@Controller
public class BuilderController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/builder/pending")
    public String pendingPage(Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return "redirect:/login";

        // Approved builder → dashboard
        if ("ROLE_BUILDER".equals(user.getRole()) && user.isApproved()) {
            return "redirect:/builder/dashboard";
        }

        return "builder-pending";
    }

    @GetMapping("/builder/dashboard")
public String builderDashboard(Authentication authentication,
                               org.springframework.ui.Model model) {

    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElse(null);

    if (user == null) return "redirect:/login";

    if (!"ROLE_BUILDER".equals(user.getRole()) || !user.isApproved()) {
        return "redirect:/builder/pending";
    }

    boolean companyExists = user.getBuilderCompany() != null;
    model.addAttribute("companyExists", companyExists);

    return "builder-dashboard";
}

}
