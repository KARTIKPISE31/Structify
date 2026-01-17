package com.structify.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.structify.backend.entity.User;
import com.structify.backend.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // ✅ ADMIN DASHBOARD
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute(
                "pendingBuilders",
                userRepository.findByRole("ROLE_PENDING_BUILDER")
        );
        return "admin-dashboard";
    }

    // ✅ APPROVE BUILDER
    @PostMapping("/approve-builder/{id}")
    public String approveBuilder(@PathVariable Long id) {

        User user = userRepository.findById(id).orElseThrow();

        // 🔥 IMPORTANT FIX
        user.setApproved(true);
        user.setRole("ROLE_BUILDER");   // MOVE FROM PENDING → BUILDER

        userRepository.save(user);

        return "redirect:/admin/dashboard";
    }
}
