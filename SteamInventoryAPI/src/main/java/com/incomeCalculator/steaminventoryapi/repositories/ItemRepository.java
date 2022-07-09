package com.incomeCalculator.steaminventoryapi.repositories;

import com.incomeCalculator.steaminventoryapi.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {

    @Override
    Optional<Item> findById(Long aLong);

    Optional<Item> findByClassId(Long classId);

}
