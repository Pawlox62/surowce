package com.surowce.service;

import com.surowce.config.DataLoader;
import com.surowce.dto.PricePointDto;
import com.surowce.entity.Surowiec;
import com.surowce.repository.PriceRepository;
import com.surowce.repository.SurowiecRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SurowiecService {

    private final SurowiecRepository surowiecRepo;
    private final PriceRepository    priceRepo;
    private final DataLoader         loader;

    public SurowiecService(SurowiecRepository surowiecRepo,
                           PriceRepository priceRepo,
                           DataLoader loader) {
        this.surowiecRepo = surowiecRepo;
        this.priceRepo    = priceRepo;
        this.loader       = loader;
    }

    /*--- główne metody ---*/

    @Transactional(readOnly = true)
    public List<Surowiec> findAll() {
        return surowiecRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Surowiec findOne(Long id) {
        return surowiecRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PricePointDto> series(Long id, LocalDate from, LocalDate to) {
        return priceRepo.series(id, from, to);
    }

    @Transactional
    public void reload() {
        priceRepo.deleteAllInBatch();
        surowiecRepo.deleteAllInBatch();
        loader.load();
    }

    /**
     * Import lub aktualizacja listy surowców.
     * Dla każdego obiektu:
     *  - jeśli w bazie istnieje wpis o tej samej nazwie → pobierz i zaktualizuj cenę,
     *  - w przeciwnym razie → wstaw nowy rekord.
     */
    @Transactional
    public void saveAll(List<Surowiec> listaSurowcow) {
        for (Surowiec su : listaSurowcow) {
            Optional<Surowiec> istniejącyOpt = surowiecRepo.findByNazwa(su.getNazwa());
            if (istniejącyOpt.isPresent()) {
                // Aktualizacja istniejącego rekordu
                Surowiec istniejący = istniejącyOpt.get();
                istniejący.setCena(su.getCena());
                surowiecRepo.save(istniejący);
            } else {
                // Nowy obiekt: usuń ewentualne ID i wstaw
                su.setId(null);
                surowiecRepo.save(su);
            }
        }
    }

    /*--- aliasy dla kontrolerów ---*/

    public List<Surowiec> all() {
        return findAll();
    }

    public Surowiec one(Long id) {
        return findOne(id);
    }

    public List<PricePointDto> prices(Long id, LocalDate from, LocalDate to) {
        return series(id, from, to);
    }
}
