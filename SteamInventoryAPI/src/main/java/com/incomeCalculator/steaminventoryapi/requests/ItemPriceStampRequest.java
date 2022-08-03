package com.incomeCalculator.steaminventoryapi.requests;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemPriceStampRequest {

    private String dateString;
    private Long itemClassId;
    private BigDecimal costUSD;
    private BigDecimal costRUB;
    private Long volume;

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Long getItemClassId() {
        return itemClassId;
    }

    public void setItemClassId(Long itemClassId) {
        this.itemClassId = itemClassId;
    }

    public BigDecimal getCostUSD() {
        return costUSD;
    }

    public void setCostUSD(BigDecimal costUSD) {
        this.costUSD = costUSD;
    }

    public BigDecimal getCostRUB() {
        return costRUB;
    }

    public void setCostRUB(BigDecimal costRUB) {
        this.costRUB = costRUB;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPriceStampRequest)) return false;
        ItemPriceStampRequest that = (ItemPriceStampRequest) o;
        return Objects.equals(dateString, that.dateString) && Objects.equals(itemClassId, that.itemClassId) && Objects.equals(costUSD, that.costUSD) && Objects.equals(costRUB, that.costRUB) && Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateString, itemClassId, costUSD, costRUB, volume);
    }

    @Override
    public String toString() {
        return "ItemPriceStampRequest{" +
                "dateString='" + dateString + '\'' +
                ", itemClassId=" + itemClassId +
                ", costUSD=" + costUSD +
                ", costRUB=" + costRUB +
                ", volume=" + volume +
                '}';
    }

}
