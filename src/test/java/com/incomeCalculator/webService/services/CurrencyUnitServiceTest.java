package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurrencyUnitServiceTest {

    @Mock
    CurrencyUnitRepository repository;
    @InjectMocks
    private CurrencyUnitService service;

    @Test
    public void getCurrencyUnitById() {
        Long id = 1L;
        String currencyName = "USD";
        long scale = 1;
        long currencyId = 432;

        Long parammode = 0L;

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,currencyName,currencyId,scale);

        when(repository.findById(id)).thenReturn(Optional.of(currencyUnit));

        CurrencyUnitEntity foundEntity = service.getCurrencyUnitWithParam(id.toString(),parammode);
        assertEquals(foundEntity,currencyUnit);
    }

    @Test
    public void getCurrencyUnitByCurrencyName() {
        Long id = 1L;
        String currencyName = "USD";
        long scale = 1;
        long currencyId = 432;

        Long parammode = 1L;

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,currencyName,currencyId,scale);

        when(repository.findByCurrencyName(currencyName)).thenReturn(Optional.of(currencyUnit));

        CurrencyUnitEntity foundEntity = service.getCurrencyUnitWithParam(currencyName,parammode);
        assertEquals(foundEntity,currencyUnit);
    }

    @Test
    public void getCurrencyUnitByCurrencyId() {
        Long id = 1L;
        String currencyName = "USD";
        long scale = 1;
        long currencyId = 432;

        Long parammode = 2L;

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,currencyName,currencyId,scale);

        when(repository.findByCurrencyId(currencyId)).thenReturn(Optional.of(currencyUnit));

        CurrencyUnitEntity foundEntity = service.getCurrencyUnitWithParam(String.valueOf(currencyId),parammode);
        assertEquals(foundEntity,currencyUnit);
    }

}