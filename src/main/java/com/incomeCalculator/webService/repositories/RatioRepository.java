package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.Ratio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatioRepository extends JpaRepository<Ratio,Long> {

    Optional<Ratio> findByCurrencyUnit_CurrencyName(String currencyName);

}
