package com.surowce.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PricePointDto {
    private final LocalDate date;
    private final BigDecimal value;

    public PricePointDto(LocalDate date, BigDecimal value) {
        this.date  = date;
        this.value = value;
    }

    public LocalDate getDate()  { return date; }
    public BigDecimal getValue(){ return value; }
}
