package com.surowce.service;

import com.surowce.entity.Konflikt;
import com.surowce.repository.KonfliktRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class KonfliktService {
    private final KonfliktRepository repo;

    public KonfliktService(KonfliktRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Konflikt> all() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Konflikt one(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Konflikt> inYears(Integer from, Integer to) {
        return repo.findByRokBetween(from, to);
    }
}
