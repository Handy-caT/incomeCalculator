package com.incomeCalculator.cardservice.repositories;

import com.incomeCalculator.cardservice.models.Ratio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatioRepository extends JpaRepository<Ratio,Long> {

    Optional<Ratio> findByCurrencyUnit_CurrencyNameAndDateString(String currencyName, String dateString);
    List<Ratio> findAllByDateString(String dateString);
    Optional<List<Ratio>> findAllByCurrencyUnit_CurrencyName(String currencyName);

    void deleteAllByDateString(String dateString);

}
