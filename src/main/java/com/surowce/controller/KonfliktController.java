package com.surowce.controller;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.surowce.entity.Konflikt;
import com.surowce.service.KonfliktService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/konflikty")
public class KonfliktController {
    private final KonfliktService service;

    public KonfliktController(KonfliktService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Konflikt> getAllJson() {
        return service.pobierzWszystkie();
    }

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public KonfliktList getAllXml() {
        return new KonfliktList(service.pobierzWszystkie());
    }

    @JacksonXmlRootElement(localName = "konflikty")
    public static class KonfliktList {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "konflikt")
        private List<Konflikt> lista;

        public KonfliktList() {
        }

        public KonfliktList(List<Konflikt> lista) {
            this.lista = lista;
        }

        public List<Konflikt> getLista() {
            return lista;
        }

        public void setLista(List<Konflikt> lista) {
            this.lista = lista;
        }
    }
}
