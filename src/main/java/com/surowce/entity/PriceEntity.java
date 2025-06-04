package com.surowce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "price")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_date")
    private LocalDate date;

    @Column(name = "price_value", precision = 18, scale = 2)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "surowiec_id")
    private Surowiec surowiec;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public Surowiec getSurowiec() { return surowiec; }
    public void setSurowiec(Surowiec surowiec) { this.surowiec = surowiec; }
}
