package com.incomeCalculator.webService.models;

import com.incomeCalculator.core.wallet.money.currencyUnit.CurrencyUnit;
import com.incomeCalculator.core.wallet.money.currencyUnit.StrictCurrencyUnit;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "CURRENCY_UNITS")
public class CurrencyUnitEntity implements CurrencyUnit {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

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
    public CurrencyUnitEntity(Long id,String currencyString, long currencyId, long scale) {
        this.id = id;
        this.currencyName = currencyString;
        this.currencyId = currencyId;
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CurrencyUnit{" + "id=" + this.id +", currencyName='" + this.currencyName + '\''
                + ", currencyId=" + this.currencyId + ", currencyScale=" + this.currencyScale + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyUnitEntity)) return false;
        CurrencyUnitEntity that = (CurrencyUnitEntity) o;
        return currencyId == that.currencyId && currencyScale == that.currencyScale && Objects.equals(id, that.id) && Objects.equals(currencyName, that.currencyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyName, currencyId, currencyScale);
    }

}
