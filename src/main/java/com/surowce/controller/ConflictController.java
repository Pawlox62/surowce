package com.surowce.controller;

import com.surowce.entity.Konflikt;
import com.surowce.service.KonfliktService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/konflikty")
public class ConflictController {

    private final KonfliktService service;

    public ConflictController(KonfliktService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Konflikt k = service.one(id);
        model.addAttribute("konflikt", k);
        return "konflikt";
    }
}
