package com.incomeCalculator.webService.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity(name = "RATIOS")
public class Ratio {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @OneToOne
    private CurrencyUnitEntity currencyUnit;

    @Column(scale = 4, precision = 6)
    private BigDecimal ratio;

    @Column(length = 10)
    private String dateString;

    public Ratio() {

    }

    public Ratio(CurrencyUnitEntity currencyUnit,BigDecimal ratio, String dateString) {
        this.currencyUnit = currencyUnit;
        this.ratio = ratio;
        this.dateString = dateString;
    }

    public Ratio(Long id, CurrencyUnitEntity currencyUnit, BigDecimal ratio, String dateString) {
        this.id = id;
        this.currencyUnit = currencyUnit;
        this.ratio = ratio;
        this.dateString = dateString;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public CurrencyUnitEntity getCurrencyUnit() {
        return currencyUnit;
    }

    public Long getId() {
        return id;
    }

    public String getDateString() {
        return dateString;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrencyUnit(CurrencyUnitEntity currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio.setScale(4, RoundingMode.HALF_DOWN);
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public String toString() {
        return "Ratio{" + "id=" + this.id +", currencyUnit=" + this.currencyUnit
                + ", ratio=" + this.ratio +", dateString=" + this.dateString + '}';
    }

}
