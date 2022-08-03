package com.incomeCalculator.steaminventoryapi.repositories;

import com.incomeCalculator.steaminventoryapi.models.Item;
import com.incomeCalculator.steaminventoryapi.models.ItemPriceStamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ItemPriceStampRepository extends JpaRepository<ItemPriceStamp, Long> {

    @Override
    Optional<ItemPriceStamp> findById(Long aLong);

    List<ItemPriceStamp> findByItem(Item item);

    List<ItemPriceStamp> findByItemAndDateStringContains(Item item,String dateString);

}
