package com.surowce.repository;

import com.surowce.entity.Surowiec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurowiecRepository extends JpaRepository<Surowiec, Long> {
    Optional<Surowiec> findByNazwa(String nazwa);
}
