package com.incomeCalculator.cardservice.repositories;

import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyUnitRepository extends JpaRepository<CurrencyUnitEntity,Long> {

    Optional<CurrencyUnitEntity> findByCurrencyName(String currencyName);
    Optional<CurrencyUnitEntity> findByCurrencyId(long currencyId);

}
