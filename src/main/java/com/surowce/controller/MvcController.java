package com.surowce.controller;

import com.surowce.entity.Konflikt;
import com.surowce.service.KonfliktService;
import com.surowce.service.SurowiecService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MvcController {

    private final SurowiecService surowiecService;
    private final KonfliktService konfliktService;

    public MvcController(SurowiecService surowiecService,
                         KonfliktService konfliktService) {
        this.surowiecService = surowiecService;
        this.konfliktService = konfliktService;
    }

    @GetMapping({"/", "/index"})
    public String showIndex(Model model) {
        model.addAttribute("surowce", surowiecService.all());
        // jeżeli w index.html nie wykorzystujesz listy konfliktów, tę linię można usunąć:
        model.addAttribute("konflikty", konfliktService.all());
        return "index";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("surowce", surowiecService.all());
        model.addAttribute("konflikty", konfliktService.all());
        return "dashboard";
    }

    @GetMapping("/konflikty/{id}")
    public String detailConflict(@PathVariable Long id, Model model) {
        Konflikt konflikt = konfliktService.one(id);
        model.addAttribute("konflikt", konflikt);
        return "konflikt";
    }
}
