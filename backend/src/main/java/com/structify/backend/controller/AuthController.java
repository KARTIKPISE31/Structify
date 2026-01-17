package com.structify.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.structify.backend.entity.User;
import com.structify.backend.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(
            @ModelAttribute("user") User user,
            @RequestParam(required = false) String builderRequest
    ) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // DEFAULT USER
        user.setRole("ROLE_USER");
        user.setApproved(true);
        user.setBuilder(false);

        // BUILDER REQUEST
        if (builderRequest != null) {
            user.setRole("ROLE_PENDING_BUILDER");
            user.setBuilder(true);
            user.setApproved(false);
        }

        userRepository.save(user);
        return "redirect:/login";
    }
}
