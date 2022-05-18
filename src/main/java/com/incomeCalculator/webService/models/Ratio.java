package com.incomeCalculator.webService.models;

import javax.persistence.*;
import java.math.BigDecimal;

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

    protected Ratio() {

    }

    public Ratio(CurrencyUnitEntity currencyUnit,BigDecimal ratio, String dateString) {
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

    @Override
    public String toString() {
        return "Ratio{" + "id=" + this.id +", currencyUnit=" + this.currencyUnit
                + ", ratio=" + this.ratio +", dateString=" + this.dateString + '}';
    }

}
