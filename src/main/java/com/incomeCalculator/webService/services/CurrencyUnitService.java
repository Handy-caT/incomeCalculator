package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CurrencyUnitService {

    private final CurrencyUnitRepository repository;


    public CurrencyUnitService(CurrencyUnitRepository repository) {
        this.repository = repository;
    }

    public CurrencyUnitEntity getCurrencyUnitWithParam(String param, Long parammode) {
        CurrencyUnitEntity currencyUnit;
        if(Objects.equals(parammode, 0L)) {
            currencyUnit = getCurrencyUnitWithParam(param);
        } else if(Objects.equals(parammode, 1L)) {
            currencyUnit = repository.findByCurrencyName(param)
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(param));
        } else if(Objects.equals(parammode, 2L)) {
            currencyUnit = repository.findByCurrencyId(Long.parseLong(param))
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(param));
        } else {
            currencyUnit = getCurrencyUnitWithParam(param);
        }
        return currencyUnit;
    }
    public CurrencyUnitEntity getCurrencyUnitWithParam(String param) {
        return repository.findById(Long.parseLong(param))
                .orElseThrow(() -> new CurrencyUnitNotFoundException(Long.parseLong(param)));
    }
}
