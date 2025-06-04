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
     * Zapisuje listę surowców (używane przez import).
     */
    @Transactional
    public void saveAll(List<Surowiec> listaSurowcow) {
        surowiecRepo.saveAll(listaSurowcow);
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
