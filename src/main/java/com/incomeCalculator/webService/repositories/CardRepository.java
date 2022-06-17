package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {

    @Override
    Optional<Card> findById(Long aLong);
    Optional<Card> findByCardName(String cardName);
    Optional<List<Card>> findByUser_Login(String login);
}
