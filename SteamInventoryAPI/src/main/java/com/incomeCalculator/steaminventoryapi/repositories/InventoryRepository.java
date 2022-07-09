package com.incomeCalculator.steaminventoryapi.repositories;

import com.incomeCalculator.steaminventoryapi.models.Inventory;
import com.incomeCalculator.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Override
    Optional<Inventory> findById(Long aLong);

    Optional<Inventory> findByUser(User user);

}
