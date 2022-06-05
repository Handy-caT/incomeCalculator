package com.incomeCalculator.webService.requests;

public class CardRequest {

    private String cardName;
    private String currencyName;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String toString() {
        return "CardRequest{" +
                "cardName='" + cardName + '\'' +
                ", currencyName='" + currencyName + '\'' +
                '}';
    }

}
