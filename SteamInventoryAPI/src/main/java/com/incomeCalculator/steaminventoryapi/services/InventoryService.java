package com.incomeCalculator.steaminventoryapi.services;

import com.incomeCalculator.steaminventoryapi.models.Inventory;
import com.incomeCalculator.steaminventoryapi.models.Item;
import com.incomeCalculator.steaminventoryapi.models.ItemPriceStamp;
import com.incomeCalculator.steaminventoryapi.repositories.InventoryRepository;
import com.incomeCalculator.steaminventoryapi.repositories.ItemRepository;
import com.incomeCalculator.steaminventoryapi.repositories.ItemPriceStampRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemPriceStampRepository priceStampRepository;

    public void countPrice(Inventory inventory) {
        List<Item> itemList = inventory.getItemList();
        BigDecimal costUSD = BigDecimal.ZERO;
        BigDecimal costRUB = BigDecimal.ZERO;

        for (Item item : itemList) {
            List<ItemPriceStamp> priceStampList =  priceStampRepository.findByItem(item);
            ItemPriceStamp priceStamp = priceStampList.get(0);

            costUSD = costUSD.add(priceStamp.getCostUSD());
            costRUB = costRUB.add(priceStamp.getCostRUB());
        }

    }

}
