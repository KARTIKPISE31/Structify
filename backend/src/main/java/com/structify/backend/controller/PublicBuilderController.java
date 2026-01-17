package com.structify.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicBuilderController {

    @GetMapping("/public/builders")
    public String viewBuilders(Model model) {
        // This controller is now safe and unused for now
        return "redirect:/builders";
    }
}
