package com.incomeCalculator.webService.models;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "RATIO")
public class Ratio {

    private @Id
    @GeneratedValue Long id;

    @OneToOne
    private CurrencyUnitEntity currencyUnit;

    @Column(scale = 4)
    private BigDecimal ratio;

    protected Ratio() {

    }

    public Ratio(CurrencyUnitEntity currencyUnit,BigDecimal ratio) {
        this.currencyUnit = currencyUnit;
        this.ratio = ratio;
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

    @Override
    public String toString() {
        return "Ratio{" + "id=" + this.id +", currencyUnit=" + this.currencyUnit
                + ", ratio=" + this.ratio + '}';
    }

}
