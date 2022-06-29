package com.incomeCalculator.cardservice.repositories;

import com.incomeCalculator.cardservice.models.Card;
import com.incomeCalculator.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {

    @Override
    Optional<Card> findById(Long aLong);
    Optional<Card> findByCardName(String cardName);
    Optional<List<Card>> findAllByUser(User user);
}
