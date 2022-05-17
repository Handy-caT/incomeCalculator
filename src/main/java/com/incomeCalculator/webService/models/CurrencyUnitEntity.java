package com.incomeCalculator.webService.models;

import com.incomeCalculator.core.wallet.money.currencyUnit.StrictCurrencyUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "CURRENCY_UNITS")
public class CurrencyUnitEntity extends StrictCurrencyUnit {

    private @Id @GeneratedValue Long id;

    @Column(length = 3)
    private String currencyName;
    @Column(unique = true)
    private long currencyId;
    private long currencyScale;

    protected CurrencyUnitEntity() {

    }

    public CurrencyUnitEntity(String currencyString, long id, long scale) {
        this.currencyName = currencyString;
        this.currencyId = id;
        this.currencyScale = scale;
    }
    public CurrencyUnitEntity(StrictCurrencyUnit currencyUnit) {
        this.currencyName = currencyUnit.getCurrencyName();
        this.currencyId = currencyUnit.getCurrencyId();
        this.currencyScale = currencyUnit.getCurrencyScale();
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public long getCurrencyScale() {
        return currencyScale;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CurrencyUnit{" + "id=" + this.id +", currencyName='" + this.currencyName + '\''
                + ", currencyId=" + this.currencyId + ", currencyScale=" + this.currencyScale + '}';
    }
}
