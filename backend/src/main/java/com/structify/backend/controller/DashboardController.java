package com.structify.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.structify.backend.entity.User;
import com.structify.backend.repository.UserRepository;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return "login";

        if ("ROLE_ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        }

        if ("ROLE_BUILDER".equals(user.getRole())) {
            if (user.isApproved()) {
                return "redirect:/builder/dashboard";
            } else {
                return "redirect:/builder/pending";
            }
        }

        return "user-dashboard";
    }
}
