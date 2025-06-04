package com.surowce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "surowiec")
public class Surowiec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nazwa;
    @Column(precision = 18, scale = 2)
    private BigDecimal cena;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }
    public BigDecimal getCena() { return cena; }
    public void setCena(BigDecimal cena) { this.cena = cena; }
}
