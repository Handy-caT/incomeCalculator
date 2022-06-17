package com.incomeCalculator.webService.requests;

import java.math.BigDecimal;

public class RatioRequest {

    private String currencyName;
    private BigDecimal ratio;
    private Long id;
    private String dateString;

    public RatioRequest(Long id,String currencyName, BigDecimal ratio, String dateString) {
        this.currencyName = currencyName;
        this.ratio = ratio;
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

}
