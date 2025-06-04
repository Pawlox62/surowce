package com.surowce.controller;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.surowce.entity.Surowiec;
import com.surowce.service.SurowiecService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/surowce")
public class SurowiecController {
    private final SurowiecService service;

    public SurowiecController(SurowiecService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Surowiec> getAllJson() {
        return service.pobierzWszystkie();
    }

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public SurowiecList getAllXml() {
        return new SurowiecList(service.pobierzWszystkie());
    }

    @JacksonXmlRootElement(localName = "surowce")
    public static class SurowiecList {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "surowiec")
        private List<Surowiec> lista;

        public SurowiecList() {
        }

        public SurowiecList(List<Surowiec> lista) {
            this.lista = lista;
        }

        public List<Surowiec> getLista() {
            return lista;
        }

        public void setLista(List<Surowiec> lista) {
            this.lista = lista;
        }
    }
}
