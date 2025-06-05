package com.surowce.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.surowce.entity.Konflikt;
import com.surowce.entity.PriceEntity;
import com.surowce.entity.Surowiec;
import com.surowce.repository.KonfliktRepository;
import com.surowce.repository.PriceRepository;
import com.surowce.repository.SurowiecRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Component
public class DataLoader {

    private final SurowiecRepository sRepo;
    private final PriceRepository   pRepo;
    private final KonfliktRepository kRepo;
    private final ObjectMapper mapper;

    public DataLoader(SurowiecRepository sRepo,
                      PriceRepository pRepo,
                      KonfliktRepository kRepo) {
        this.sRepo  = sRepo;
        this.pRepo  = pRepo;
        this.kRepo  = kRepo;
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @PostConstruct
    public void init() {
        if (sRepo.count() == 0 || pRepo.count() == 0 || kRepo.count() == 0) {
            load();
        }
    }

    public void load() {
        pRepo.deleteAllInBatch();
        sRepo.deleteAllInBatch();
        kRepo.deleteAllInBatch();

        Map<String, Surowiec> cache = new HashMap<>();
        loadPrices(cache);
        loadConflicts();
    }

    private void loadPrices(Map<String, Surowiec> cache) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/ceny.json")) {
            if (is == null) return;

            List<Map<String, Object>> rows =
                    mapper.readValue(is, new TypeReference<>() {});

            for (Map<String, Object> row : rows) {
                LocalDate date = LocalDate.parse(row.get("Date").toString());
                for (var e : row.entrySet()) {
                    if ("Date".equals(e.getKey()) || e.getValue() == null) continue;

                    BigDecimal val = new BigDecimal(e.getValue().toString())
                            .setScale(2, RoundingMode.HALF_UP);

                    Surowiec su = cache.computeIfAbsent(e.getKey(), k -> {
                        Surowiec n = new Surowiec();
                        n.setNazwa(k);
                        n.setCena(val);
                        return sRepo.save(n);
                    });
                    su.setCena(val);
                    sRepo.save(su);

                    PriceEntity p = new PriceEntity();
                    p.setSurowiec(su);
                    p.setDate(date);
                    p.setValue(val);
                    pRepo.save(p);
                }
            }
        } catch (Exception ignored) { }
    }

    private void loadConflicts() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/konflikty.csv")) {
            if (is == null) return;

            try (CSVReader reader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String[] header = reader.readNext();
                if (header == null) return;

                Map<String, Integer> idx = new HashMap<>();
                for (int i = 0; i < header.length; i++) {
                    idx.put(header[i], i);
                }

                String[] row;
                while ((row = reader.readNext()) != null) {
                    String yearStr = row[idx.get("year")];
                    if (!yearStr.matches("\\d+")) continue;

                    Konflikt k = new Konflikt();
                    k.setConflictId(row[idx.get("conflict_id")]);
                    k.setLocation(row[idx.get("location")]);
                    k.setSideA(row[idx.get("side_a")]);
                    k.setSideB(row[idx.get("side_b")]);
                    k.setRok(Integer.parseInt(yearStr));

                    String sd = row[idx.get("start_date")];
                    if (sd != null && !sd.isEmpty()) {
                        try {
                            k.setStartDate(LocalDate.parse(sd));
                        } catch (Exception ignored) { }
                    }

                    String ed = row[idx.get("ep_end_date")];
                    if (ed != null && !ed.isEmpty()) {
                        try {
                            k.setEpEndDate(LocalDate.parse(ed));
                        } catch (Exception ignored) { }
                    }

                    kRepo.save(k);
                }
            }
        } catch (Exception ignored) { }
    }
}
