package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {



}
