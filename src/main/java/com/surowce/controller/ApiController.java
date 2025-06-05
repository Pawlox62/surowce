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

    @GetMapping(
            value = "/surowce",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Surowiec> surowce() {
        return surowiecSvc.all();
    }

    @GetMapping(
            value = "/surowce/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Surowiec surowiec(@PathVariable Long id) {
        return surowiecSvc.one(id);
    }

    @GetMapping(
            value = "/surowce/{id}/prices",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PricePointDto> prices(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return surowiecSvc.prices(id, from, to);
    }

    /* --- EKSPORT / IMPORT SUROWCE (JSON | XML) --- */

    @GetMapping(
            value = "/surowce/export",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public List<Surowiec> exportSurowce() {
        return surowiecSvc.all();
    }

    @PostMapping(
            value = "/surowce/import",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<Void> importSurowce(@RequestBody List<Surowiec> body) {
        surowiecSvc.saveAll(body);
        return ResponseEntity.accepted().build();
    }

    /* ---------- KONFLIKTY ---------- */

    @GetMapping(
            value = "/konflikty",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Konflikt> konflikty() {
        return konfliktSvc.all();
    }

    @GetMapping(
            value = "/konflikty/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Konflikt konflikt(@PathVariable Long id) {
        return konfliktSvc.one(id);
    }

    @GetMapping(
            value = "/konflikty/range",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Konflikt> konfliktyInRange(
            @RequestParam Integer from,
            @RequestParam Integer to
    ) {
        return konfliktSvc.inYears(from, to);
    }

    /* --- EKSPORT / IMPORT KONFLIKTY (JSON | XML) --- */

    @GetMapping(
            value = "/konflikty/export",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public List<Konflikt> exportKonflikty() {
        return konfliktSvc.all();
    }

    @PostMapping(
            value = "/konflikty/import",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<Void> importKonflikty(@RequestBody List<Konflikt> body) {
        konfliktSvc.saveAll(body);
        return ResponseEntity.accepted().build();
    }
}
