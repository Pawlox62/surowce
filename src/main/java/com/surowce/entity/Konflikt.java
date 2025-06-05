package com.surowce.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "konflikt")
public class Konflikt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String conflictId;
    private String location;
    @Column(columnDefinition = "TEXT")
    private String sideA;
    @Column(columnDefinition = "TEXT")
    private String sideB;
    private Integer rok;
    private LocalDate startDate;
    private LocalDate epEndDate;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConflictId() { return conflictId; }
    public void setConflictId(String conflictId) { this.conflictId = conflictId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSideA() { return sideA; }
    public void setSideA(String sideA) { this.sideA = sideA; }

    public String getSideB() { return sideB; }
    public void setSideB(String sideB) { this.sideB = sideB; }

    public Integer getRok() { return rok; }
    public void setRok(Integer rok) { this.rok = rok; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEpEndDate() { return epEndDate; }
    public void setEpEndDate(LocalDate epEndDate) { this.epEndDate = epEndDate; }
}
