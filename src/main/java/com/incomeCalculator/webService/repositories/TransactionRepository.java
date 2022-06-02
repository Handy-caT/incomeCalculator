package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionModel,Long> {



}
