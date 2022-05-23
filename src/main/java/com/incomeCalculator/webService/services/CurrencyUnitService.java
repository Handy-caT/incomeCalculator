package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Component
public class CurrencyUnitService {

    private CurrencyUnitRepository repository;


    public CurrencyUnitService(CurrencyUnitRepository repository) {
        this.repository = repository;
    }

    public CurrencyUnitEntity getCurrencyUnitWithParam(String param, Long parammode) {
        CurrencyUnitEntity currencyUnit;
        if(Objects.equals(parammode, 0)) {
            currencyUnit = repository.findById(Long.parseLong(param))
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(Long.parseLong(param)));
        } else if(Objects.equals(parammode, 1)) {
            currencyUnit = repository.findByCurrencyName(param)
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(param));
        } else if(Objects.equals(parammode, 2)) {
            currencyUnit = repository.findByCurrencyId(Long.parseLong(param))
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(param));
        } else {
            currencyUnit = repository.findById(Long.parseLong(param))
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(Long.parseLong(param)));
        }
        return currencyUnit;
    }
}
