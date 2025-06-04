package com.surowce.repository;

import com.surowce.entity.Konflikt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KonfliktRepository extends JpaRepository<Konflikt, Long> {
    List<Konflikt> findByRokBetween(Integer from, Integer to);
}
