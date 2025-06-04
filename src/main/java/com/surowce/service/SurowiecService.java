package com.surowce.service;

import com.surowce.entity.Surowiec;
import com.surowce.repository.SurowiecRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SurowiecService {
    private final SurowiecRepository repo;

    public SurowiecService(SurowiecRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Surowiec> pobierzWszystkie() {
        return repo.findAll();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void zapiszSurowce(List<Surowiec> lista) {
        repo.saveAll(lista);
    }
}
