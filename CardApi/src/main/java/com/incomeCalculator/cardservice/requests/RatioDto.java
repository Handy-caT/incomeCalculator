package com.incomeCalculator.cardservice.requests;

import java.math.BigDecimal;

public class RatioDto {

    private String currencyName;
    private BigDecimal ratio;
    private String dateString;

    public RatioDto(String currencyName, BigDecimal ratio, String dateString) {
        this.currencyName = currencyName;
        this.ratio = ratio;
        this.dateString = dateString;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

}
