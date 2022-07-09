package com.incomeCalculator.steaminventoryapi.models;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "PRICES")
public class ItemPriceStamp {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @ManyToOne
    private Item item;

    private Long costUSD;

    private Long costRUB;

    private Long volume;

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
