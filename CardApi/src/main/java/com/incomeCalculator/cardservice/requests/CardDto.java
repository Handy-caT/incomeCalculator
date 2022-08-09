package com.incomeCalculator.cardservice.requests;

public class CardDto {

    private String cardName;
    private String currencyName;

    public CardDto() {
    }

    public CardDto(String cardName, String currencyName) {
        this.cardName = cardName;
        this.currencyName = currencyName;
    }

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
