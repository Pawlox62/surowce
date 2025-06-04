package com.surowce.controller;

import com.surowce.service.SurowiecService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImpactController {
    private final SurowiecService service;

    public ImpactController(SurowiecService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String impact(Model model) {
        model.addAttribute("surowce", service.all());
        return "impact";
    }
}
