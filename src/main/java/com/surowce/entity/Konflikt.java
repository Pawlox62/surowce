package com.surowce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Konflikt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conflictId;         // conflict_id
    private String location;           // location
    private String sideA;              // side_a
    private String sideAId;            // side_a_id
    private String sideA2nd;           // side_a_2nd
    private String sideB;              // side_b
    private String sideBId;            // side_b_id
    private String sideB2nd;           // side_b_2nd
    private String incompatibility;    // incompatibility
    private String territoryName;      // territory_name

    // zamiast 'private String year;', zmieniamy pole na 'rok'
    @Column(name = "rok")            // tak by w bazie kolumna nazywała się 'rok'
    private String rok;               // odpowiada CSV ‘year’

    private String intensityLevel;     // intensity_level
    private String cumulativeIntensity;// cumulative_intensity
    private String typeOfConflict;     // type_of_conflict
    private String startDate;          // start_date
    private String startPrec;          // start_prec
    private String startDate2;         // start_date2
    private String startPrec2;         // start_prec2
    private String epEnd;              // ep_end
    private String epEndDate;          // ep_end_date
    private String epEndPrec;          // ep_end_prec
    private String gwnoA;              // gwno_a
    private String gwnoA2nd;           // gwno_a_2nd
    private String gwnoB;              // gwno_b
    private String gwnoB2nd;           // gwno_b_2nd
    private String gwnoLoc;            // gwno_loc
    private String region;             // region
    private String version;            // version

    // ======= gettery i settery =======

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getConflictId() {
        return conflictId;
    }
    public void setConflictId(String conflictId) {
        this.conflictId = conflictId;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getSideA() {
        return sideA;
    }
    public void setSideA(String sideA) {
        this.sideA = sideA;
    }

    public String getSideAId() {
        return sideAId;
    }
    public void setSideAId(String sideAId) {
        this.sideAId = sideAId;
    }

    public String getSideA2nd() {
        return sideA2nd;
    }
    public void setSideA2nd(String sideA2nd) {
        this.sideA2nd = sideA2nd;
    }

    public String getSideB() {
        return sideB;
    }
    public void setSideB(String sideB) {
        this.sideB = sideB;
    }

    public String getSideBId() {
        return sideBId;
    }
    public void setSideBId(String sideBId) {
        this.sideBId = sideBId;
    }

    public String getSideB2nd() {
        return sideB2nd;
    }
    public void setSideB2nd(String sideB2nd) {
        this.sideB2nd = sideB2nd;
    }

    public String getIncompatibility() {
        return incompatibility;
    }
    public void setIncompatibility(String incompatibility) {
        this.incompatibility = incompatibility;
    }

    public String getTerritoryName() {
        return territoryName;
    }
    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    // getter i setter dla ‘rok’ (wcześniej pola year)
    public String getRok() {
        return rok;
    }
    public void setRok(String rok) {
        this.rok = rok;
    }

    public String getIntensityLevel() {
        return intensityLevel;
    }
    public void setIntensityLevel(String intensityLevel) {
        this.intensityLevel = intensityLevel;
    }

    public String getCumulativeIntensity() {
        return cumulativeIntensity;
    }
    public void setCumulativeIntensity(String cumulativeIntensity) {
        this.cumulativeIntensity = cumulativeIntensity;
    }

    public String getTypeOfConflict() {
        return typeOfConflict;
    }
    public void setTypeOfConflict(String typeOfConflict) {
        this.typeOfConflict = typeOfConflict;
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartPrec() {
        return startPrec;
    }
    public void setStartPrec(String startPrec) {
        this.startPrec = startPrec;
    }

    public String getStartDate2() {
        return startDate2;
    }
    public void setStartDate2(String startDate2) {
        this.startDate2 = startDate2;
    }

    public String getStartPrec2() {
        return startPrec2;
    }
    public void setStartPrec2(String startPrec2) {
        this.startPrec2 = startPrec2;
    }

    public String getEpEnd() {
        return epEnd;
    }
    public void setEpEnd(String epEnd) {
        this.epEnd = epEnd;
    }

    public String getEpEndDate() {
        return epEndDate;
    }
    public void setEpEndDate(String epEndDate) {
        this.epEndDate = epEndDate;
    }

    public String getEpEndPrec() {
        return epEndPrec;
    }
    public void setEpEndPrec(String epEndPrec) {
        this.epEndPrec = epEndPrec;
    }

    public String getGwnoA() {
        return gwnoA;
    }
    public void setGwnoA(String gwnoA) {
        this.gwnoA = gwnoA;
    }

    public String getGwnoA2nd() {
        return gwnoA2nd;
    }
    public void setGwnoA2nd(String gwnoA2nd) {
        this.gwnoA2nd = gwnoA2nd;
    }

    public String getGwnoB() {
        return gwnoB;
    }
    public void setGwnoB(String gwnoB) {
        this.gwnoB = gwnoB;
    }

    public String getGwnoB2nd() {
        return gwnoB2nd;
    }
    public void setGwnoB2nd(String gwnoB2nd) {
        this.gwnoB2nd = gwnoB2nd;
    }

    public String getGwnoLoc() {
        return gwnoLoc;
    }
    public void setGwnoLoc(String gwnoLoc) {
        this.gwnoLoc = gwnoLoc;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}
