package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyUnitRepository extends JpaRepository<CurrencyUnitEntity,Long> {

}
