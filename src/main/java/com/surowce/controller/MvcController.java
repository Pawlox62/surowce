package com.surowce.controller;

import com.surowce.entity.Surowiec;
import com.surowce.service.SurowiecService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MvcController {
    private final SurowiecService service;

    public MvcController(SurowiecService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String pokazStrone(Model model) {
        List<Surowiec> lista = service.pobierzWszystkie();
        model.addAttribute("surowce", lista);
        return "index";
    }
}
