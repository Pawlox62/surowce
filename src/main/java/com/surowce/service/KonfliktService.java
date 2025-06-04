package com.surowce.service;

import com.surowce.entity.Konflikt;
import com.surowce.repository.KonfliktRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KonfliktService {
    private final KonfliktRepository repo;

    public KonfliktService(KonfliktRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Konflikt> pobierzWszystkie() {
        return repo.findAll();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void zapiszKonflikty(List<Konflikt> lista) {
        repo.saveAll(lista);
    }
}
