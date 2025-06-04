package com.surowce.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surowce.entity.Konflikt;
import com.surowce.entity.Surowiec;
import com.surowce.repository.KonfliktRepository;
import com.surowce.repository.SurowiecRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(SurowiecRepository surowiecRepo,
                                      KonfliktRepository konfliktRepo) {
        return args -> {
            // =====================================================
            // 1. Wczytywanie cen z pliku ceny.json ze ścieżki classpath
            // =====================================================
            try {
                // Konfiguracja Jacksona
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                // Otwórz zasób z classpath (data/ceny.json w folderze resources)
                try (InputStream is =
                             getClass().getClassLoader().getResourceAsStream("data/ceny.json")) {
                    if (is == null) {
                        System.err.println("—> Nie mogę znaleźć zasobu 'data/ceny.json' na classpath");
                    } else {
                        // Deserializujemy do List<LinkedHashMap<String,Object>>
                        List<LinkedHashMap<String, Object>> rawList =
                                mapper.readValue(is, new TypeReference<>() {});

                        for (Map<String, Object> row : rawList) {
                            Object dateObj = row.get("Date");
                            if (dateObj == null) {
                                // jeżeli wiersz nie ma daty, pomiń
                                continue;
                            }
                            String date = dateObj.toString();

                            // Iteruj po kluczach op≤rócz "Date"
                            for (Map.Entry<String, Object> entry : row.entrySet()) {
                                String key = entry.getKey();
                                if ("Date".equals(key)) {
                                    continue;
                                }
                                Object valueObj = entry.getValue();
                                if (valueObj == null) {
                                    continue;
                                }
                                Double price;
                                try {
                                    price = Double.valueOf(valueObj.toString());
                                } catch (NumberFormatException e) {
                                    continue;
                                }
                                Surowiec s = new Surowiec();
                                s.setNazwa(key + " (" + date + ")");
                                s.setCena(price);
                                surowiecRepo.save(s);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("—> Błąd podczas wczytywania pliku ceny.json:");
                e.printStackTrace();
            }

            // =====================================================
            // 2. Wczytywanie konfliktów z pliku konflikty.csv z classpath
            // =====================================================
            try {
                // Otwórz zasób z classpath (data/konflikty.csv w folderze resources)
                try (InputStream is =
                             getClass().getClassLoader().getResourceAsStream("data/konflikty.csv")) {
                    if (is == null) {
                        System.err.println("—> Nie mogę znaleźć zasobu 'data/konflikty.csv' na classpath");
                        return;
                    }

                    // Odczyt linii przy pomocy BufferedReader
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, StandardCharsets.UTF_8))) {

                        // 2.1. Pierwsza linia = nagłówki CSV
                        String headerLine = reader.readLine();
                        if (headerLine == null) {
                            System.out.println("Plik konflikty.csv jest pusty.");
                            return;
                        }

                        // 2.2. Zbuduj mapę: nagłówek → indeks kolumny
                        String[] headers = headerLine.split(",");
                        Map<String, Integer> idx = new java.util.HashMap<>();
                        for (int i = 0; i < headers.length; i++) {
                            String col = headers[i].replaceAll("^\"|\"$", "").trim();
                            idx.put(col, i);
                        }

                        // 2.3. (Opcjonalnie) Sprawdź wszystkie wymagane nagłówki
                        String[] expectedColumns = {
                                "conflict_id","location","side_a","side_a_id","side_a_2nd","side_b","side_b_id","side_b_2nd",
                                "incompatibility","territory_name","year","intensity_level","cumulative_intensity",
                                "type_of_conflict","start_date","start_prec","start_date2","start_prec2",
                                "ep_end","ep_end_date","ep_end_prec","gwno_a","gwno_a_2nd","gwno_b","gwno_b_2nd",
                                "gwno_loc","region","version"
                        };
                        for (String col : expectedColumns) {
                            if (!idx.containsKey(col)) {
                                System.out.println("UWAGA: Brak kolumny w konflikty.csv → " + col);
                            }
                        }

                        // 2.4. Czytaj wiersz po wierszu
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",", -1);
                            for (int i = 0; i < parts.length; i++) {
                                parts[i] = parts[i].replaceAll("^\"|\"$", "").trim();
                            }

                            Konflikt k = new Konflikt();
                            if (idx.containsKey("conflict_id")) {
                                k.setConflictId(parts[idx.get("conflict_id")]);
                            }
                            if (idx.containsKey("location")) {
                                k.setLocation(parts[idx.get("location")]);
                            }
                            if (idx.containsKey("side_a")) {
                                k.setSideA(parts[idx.get("side_a")]);
                            }
                            if (idx.containsKey("side_a_id")) {
                                k.setSideAId(parts[idx.get("side_a_id")]);
                            }
                            if (idx.containsKey("side_a_2nd")) {
                                k.setSideA2nd(parts[idx.get("side_a_2nd")]);
                            }
                            if (idx.containsKey("side_b")) {
                                k.setSideB(parts[idx.get("side_b")]);
                            }
                            if (idx.containsKey("side_b_id")) {
                                k.setSideBId(parts[idx.get("side_b_id")]);
                            }
                            if (idx.containsKey("side_b_2nd")) {
                                k.setSideB2nd(parts[idx.get("side_b_2nd")]);
                            }
                            if (idx.containsKey("incompatibility")) {
                                k.setIncompatibility(parts[idx.get("incompatibility")]);
                            }
                            if (idx.containsKey("territory_name")) {
                                k.setTerritoryName(parts[idx.get("territory_name")]);
                            }
                            // w encji pole nazywa się 'rok', nie 'year'
                            if (idx.containsKey("year")) {
                                k.setRok(parts[idx.get("year")]);
                            }
                            if (idx.containsKey("intensity_level")) {
                                k.setIntensityLevel(parts[idx.get("intensity_level")]);
                            }
                            if (idx.containsKey("cumulative_intensity")) {
                                k.setCumulativeIntensity(parts[idx.get("cumulative_intensity")]);
                            }
                            if (idx.containsKey("type_of_conflict")) {
                                k.setTypeOfConflict(parts[idx.get("type_of_conflict")]);
                            }
                            if (idx.containsKey("start_date")) {
                                k.setStartDate(parts[idx.get("start_date")]);
                            }
                            if (idx.containsKey("start_prec")) {
                                k.setStartPrec(parts[idx.get("start_prec")]);
                            }
                            if (idx.containsKey("start_date2")) {
                                k.setStartDate2(parts[idx.get("start_date2")]);
                            }
                            if (idx.containsKey("start_prec2")) {
                                k.setStartPrec2(parts[idx.get("start_prec2")]);
                            }
                            if (idx.containsKey("ep_end")) {
                                k.setEpEnd(parts[idx.get("ep_end")]);
                            }
                            if (idx.containsKey("ep_end_date")) {
                                k.setEpEndDate(parts[idx.get("ep_end_date")]);
                            }
                            if (idx.containsKey("ep_end_prec")) {
                                k.setEpEndPrec(parts[idx.get("ep_end_prec")]);
                            }
                            if (idx.containsKey("gwno_a")) {
                                k.setGwnoA(parts[idx.get("gwno_a")]);
                            }
                            if (idx.containsKey("gwno_a_2nd")) {
                                k.setGwnoA2nd(parts[idx.get("gwno_a_2nd")]);
                            }
                            if (idx.containsKey("gwno_b")) {
                                k.setGwnoB(parts[idx.get("gwno_b")]);
                            }
                            if (idx.containsKey("gwno_b_2nd")) {
                                k.setGwnoB2nd(parts[idx.get("gwno_b_2nd")]);
                            }
                            if (idx.containsKey("gwno_loc")) {
                                k.setGwnoLoc(parts[idx.get("gwno_loc")]);
                            }
                            if (idx.containsKey("region")) {
                                k.setRegion(parts[idx.get("region")]);
                            }
                            if (idx.containsKey("version")) {
                                k.setVersion(parts[idx.get("version")]);
                            }

                            konfliktRepo.save(k);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("—> Błąd podczas wczytywania pliku konflikty.csv:");
                e.printStackTrace();
            }
        };
    }
}
