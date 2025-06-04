package com.surowce.controller;

import com.surowce.dto.PricePointDto;
import com.surowce.entity.Konflikt;
import com.surowce.entity.Surowiec;
import com.surowce.service.KonfliktService;
import com.surowce.service.SurowiecService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final SurowiecService surowiecSvc;
    private final KonfliktService konfliktSvc;

    public ApiController(SurowiecService surowiecSvc, KonfliktService konfliktSvc) {
        this.surowiecSvc = surowiecSvc;
        this.konfliktSvc = konfliktSvc;
    }

    /* ---------- SUROWCE ---------- */

    @GetMapping("/surowce")
    public List<Surowiec> surowce() {
        return surowiecSvc.all();
    }

    @GetMapping("/surowce/{id}")
    public Surowiec surowiec(@PathVariable Long id) {
        return surowiecSvc.one(id);
    }

    @GetMapping("/surowce/{id}/prices")
    public List<PricePointDto> prices(@PathVariable Long id,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return surowiecSvc.prices(id, from, to);
    }

    /* --- NOWE: eksport / import JSON | XML --- */

    @GetMapping(
            value = "/surowce/export",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public List<Surowiec> exportSurowce() {
        // zwracamy pełną listę – format decyduje nagłówek Accept
        return surowiecSvc.all();
    }

    @PostMapping(
            value = "/surowce/import",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Void> importSurowce(@RequestBody List<Surowiec> body) {
        surowiecSvc.saveAll(body);
        return ResponseEntity.accepted().build();
    }

    /* ---------- KONFLIKTY ---------- */

    @GetMapping("/konflikty")
    public List<Konflikt> konflikty() {
        return konfliktSvc.all();
    }

    @GetMapping("/konflikty/{id}")
    public Konflikt konflikt(@PathVariable Long id) {
        return konfliktSvc.one(id);
    }

    @GetMapping("/konflikty/range")
    public List<Konflikt> konfliktyInRange(@RequestParam Integer from,
                                           @RequestParam Integer to) {
        return konfliktSvc.inYears(from, to);
    }
}
