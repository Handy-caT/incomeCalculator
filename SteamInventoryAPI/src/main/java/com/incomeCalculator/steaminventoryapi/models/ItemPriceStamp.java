package com.incomeCalculator.steaminventoryapi.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "PRICES")
@Table(indexes = {
        @Index(name = "itemIndex", columnList = "item"),
        @Index(name = "idx_itempricestamp_datestring", columnList = "dateString"),
        @Index(name = "idx_itempricestamp_item_id", columnList = "item_id")
})
public class ItemPriceStamp {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @ManyToOne
    private Item item;

    private BigDecimal costUSD;

    private BigDecimal costRUB;

    private Long volume;

    @Column(length = 19)
    private String dateString;

    public ItemPriceStamp(Long id, Item item, BigDecimal costUSD, BigDecimal costRUB, Long volume, String dateString) {
        this.id = id;
        this.item = item;
        this.costUSD = costUSD;
        this.costRUB = costRUB;
        this.volume = volume;
        this.dateString = dateString;
    }

    public ItemPriceStamp(Item item, BigDecimal costUSD, BigDecimal costRUB, Long volume, String dateString) {
        this.item = item;
        this.costUSD = costUSD;
        this.costRUB = costRUB;
        this.volume = volume;
        this.dateString = dateString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPriceStamp)) return false;
        ItemPriceStamp that = (ItemPriceStamp) o;
        return Objects.equals(id, that.id) && Objects.equals(item, that.item) && Objects.equals(costUSD, that.costUSD) && Objects.equals(costRUB, that.costRUB) && Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, costUSD, costRUB, volume);
    }

    @Override
    public String toString() {
        return "ItemPriceStamp{" +
                "id=" + id +
                ", item=" + item +
                ", costUSD=" + costUSD +
                ", costRUB=" + costRUB +
                ", volume=" + volume +
                '}';
    }

}
