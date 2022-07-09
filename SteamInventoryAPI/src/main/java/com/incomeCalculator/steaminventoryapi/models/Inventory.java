package com.incomeCalculator.steaminventoryapi.models;

import com.incomeCalculator.core.wallet.money.Money;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "INVENTORIES")
public class Inventory {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    private String inventoryUrl;

    private BigDecimal costUSD;

    private BigDecimal costRUB;

    @OneToMany
    private List<Item> itemList;

    public Inventory(Long id, String inventoryUrl, BigDecimal costUSD, BigDecimal costRUB, List<Item> itemList) {
        this.id = id;
        this.inventoryUrl = inventoryUrl;
        this.costUSD = costUSD;
        this.costRUB = costRUB;
        this.itemList = itemList;
    }

    public Inventory(String inventoryUrl, BigDecimal costUSD, BigDecimal costRUB, List<Item> itemList) {
        this.inventoryUrl = inventoryUrl;
        this.costUSD = costUSD;
        this.costRUB = costRUB;
        this.itemList = itemList;
    }

    public Inventory(String inventoryUrl, BigDecimal costUSD, BigDecimal costRUB) {
        this.inventoryUrl = inventoryUrl;
        this.costUSD = costUSD;
        this.costRUB = costRUB;
        this.itemList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInventoryUrl() {
        return inventoryUrl;
    }

    public void setInventoryUrl(String inventoryUrl) {
        this.inventoryUrl = inventoryUrl;
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

    public List<Item> getItemList() {
        return itemList;
    }

    public void addItem(Item item) {
        this.itemList.add(item);
    }

    public void deleteItem(Item item) {
        this.itemList.remove(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(id, inventory.id) && Objects.equals(inventoryUrl, inventory.inventoryUrl) && Objects.equals(costUSD, inventory.costUSD) && Objects.equals(costRUB, inventory.costRUB) && Objects.equals(itemList, inventory.itemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inventoryUrl, costUSD, costRUB, itemList);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", inventoryUrl='" + inventoryUrl + '\'' +
                ", costUSD=" + costUSD +
                ", costRUB=" + costRUB +
                ", itemList=" + itemList +
                '}';
    }

}
