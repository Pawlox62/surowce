package com.surowce.repository;

import com.surowce.dto.PricePointDto;
import com.surowce.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {
    @Query("""
           select new com.surowce.dto.PricePointDto(p.date,p.value)
           from PriceEntity p
           where p.surowiec.id=:id and p.date between :from and :to
           order by p.date
           """)
    List<PricePointDto> series(@Param("id") Long id,
                               @Param("from") LocalDate from,
                               @Param("to") LocalDate to);
}
