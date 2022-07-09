package com.incomeCalculator.steaminventoryapi.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "PRICES")
@Table(indexes = {
        @Index(name = "itemIndex", columnList = "item")
})
public class ItemPriceStamp {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @ManyToOne
    private Item item;

    private Long costUSD;

    private Long costRUB;

    private Long volume;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    public ItemPriceStamp(Long id, Item item, Long costUSD, Long costRUB, Long volume) {
        this.id = id;
        this.item = item;
        this.costUSD = costUSD;
        this.costRUB = costRUB;
        this.volume = volume;
    }

    public ItemPriceStamp(Item item, Long costUSD, Long costRUB, Long volume) {
        this.item = item;
        this.costUSD = costUSD;
        this.costRUB = costRUB;
        this.volume = volume;
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

    public Long getCostUSD() {
        return costUSD;
    }

    public void setCostUSD(Long costUSD) {
        this.costUSD = costUSD;
    }

    public Long getCostRUB() {
        return costRUB;
    }

    public void setCostRUB(Long costRUB) {
        this.costRUB = costRUB;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
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
