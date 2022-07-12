package com.incomeCalculator.steaminventoryapi.services;

import com.incomeCalculator.steaminventoryapi.models.Inventory;
import com.incomeCalculator.steaminventoryapi.models.Item;
import com.incomeCalculator.steaminventoryapi.models.ItemPriceStamp;
import com.incomeCalculator.steaminventoryapi.repositories.InventoryRepository;
import com.incomeCalculator.steaminventoryapi.repositories.ItemRepository;
import com.incomeCalculator.steaminventoryapi.repositories.PriceStampRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PriceStampRepository priceStampRepository;

    public void countPrice(Inventory inventory) {
        List<Item> itemList = inventory.getItemList();
        BigDecimal costUSD = BigDecimal.ZERO;
        BigDecimal costRUB = BigDecimal.ZERO;

        for (Item item : itemList) {
            ItemPriceStamp priceStamp = priceStampRepository.findByItem(item);
            costUSD = costUSD.add(priceStamp.getCostUSD());
            costRUB = costRUB.add(priceStamp.getCostRUB());
        }

    }

}
