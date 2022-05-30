package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Long> {


}
