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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(SurowiecRepository surowiecRepo,
                                      KonfliktRepository konfliktRepo) {
        return args -> {
            // ============================================
            // 1. Wczytywanie cen z pliku ceny.json
            // ============================================
            try {
                // 1.1. Konfiguracja ObjectMapper (ignore unknown properties)
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                // 1.2. Otwórz strumień do pliku 'src/main/resources/data/ceny.json'
                //      Używamy Files.newInputStream(Paths.get(...)), bo plik jest w resources
                String jsonPath = "src/main/resources/data/ceny.json";
                try (InputStream is = Files.newInputStream(Paths.get(jsonPath))) {
                    // 1.3. Deserializujemy listę obiektów JSON na List<LinkedHashMap<String,Object>>
                    //      Zakładam, że każda pozycja w JSON ma strukturę: { "Date": "...", "Aluminum": 2000.0, "Copper": 7000.0, ... }
                    List<LinkedHashMap<String, Object>> rawList =
                            mapper.readValue(is, new TypeReference<>() {});

                    // 1.4. Iterujemy wiersz po wierszu
                    for (Map<String, Object> row : rawList) {
                        // Klucz "Date" oznacza datę, reszta to nazwy surowców → wartości cen
                        Object dateObj = row.get("Date");
                        if (dateObj == null) {
                            continue; // jeżeli nie ma daty, pomiń wiersz
                        }
                        String date = dateObj.toString();

                        // Teraz iterujemy po wszystkich polach tego JSONa poza "Date"
                        for (Map.Entry<String, Object> entry : row.entrySet()) {
                            String key = entry.getKey();
                            if ("Date".equals(key)) {
                                continue;
                            }

                            Object valueObj = entry.getValue();
                            if (valueObj == null) {
                                continue;
                            }

                            // Parsujemy cenę na Double (jeśli nie da się sparsować, pomijamy)
                            Double price;
                            try {
                                price = Double.valueOf(valueObj.toString());
                            } catch (NumberFormatException e) {
                                continue;
                            }

                            // Tworzymy encję Surowiec i zapisujemy do bazy
                            Surowiec s = new Surowiec();
                            // W polu 'nazwa' przechowujemy np. "Aluminum (2020-01-01)"
                            s.setNazwa(key + " (" + date + ")");
                            s.setCena(price);
                            surowiecRepo.save(s);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("—> Błąd podczas wczytywania pliku ceny.json:");
                e.printStackTrace();
            }

            // ============================================
            // 2. Wczytywanie konfliktów z pliku konflikty.csv
            // ============================================
            try {
                String csvPath = "src/main/resources/data/konflikty.csv";
                try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvPath))) {
                    // 2.1. Odczyt nagłówków z pierwszej linii CSV
                    String headerLine = reader.readLine();
                    if (headerLine == null) {
                        System.out.println("Plik konflikty.csv jest pusty lub nie istnieje.");
                        return;
                    }

                    // 2.2. Tworzymy mapę: nazwa_kolumny → indeks wiersza
                    String[] headers = headerLine.split(",");
                    Map<String, Integer> idx = new java.util.HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        String col = headers[i].replaceAll("^\"|\"$", "").trim();
                        idx.put(col, i);
                    }

                    // 2.3. (Opcjonalnie) Sprawdźmy, czy wszystkie istotne nagłówki są w idx
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

                    // 2.4. Czytajmy kolejne wiersze CSV
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Rozbijamy wiersz, używając split z -1 (by uwzględnić puste kolumny)
                        String[] parts = line.split(",", -1);
                        // Obcinamy ewentualne cudzysłowy i spacje w każdej części
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].replaceAll("^\"|\"$", "").trim();
                        }

                        // Tworzymy obiekt i wypełniamy wszystkie pola:
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
                        // Uwaga: w Javie zapisujemy do pola 'rok' (adnotacja @Column(name="rok"))
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

                        // Zapisujemy do bazy:
                        konfliktRepo.save(k);
                    }
                }
            } catch (Exception e) {
                System.err.println("—> Błąd podczas wczytywania pliku konflikty.csv:");
                e.printStackTrace();
            }
        };
    }
}
