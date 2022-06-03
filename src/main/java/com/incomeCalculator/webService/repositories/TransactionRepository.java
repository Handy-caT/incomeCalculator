package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

    Optional<List<TransactionEntity>> findAllByCard(Card card);

}
