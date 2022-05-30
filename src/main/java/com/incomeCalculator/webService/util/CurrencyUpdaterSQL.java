package com.incomeCalculator.webService.util;

import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class CurrencyUpdaterSQL implements CurrencyUpdater {

    private final RatioRepository ratioRepository;
    private final CurrencyUnitRepository currencyUnitRepository;

    public CurrencyUpdaterSQL(RatioRepository ratioRepository, CurrencyUnitRepository currencyUnitRepository) {
        this.ratioRepository = ratioRepository;
        this.currencyUnitRepository = currencyUnitRepository;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        Date date = new Date();
        String dateString = DateFormatter.sqlFormat(date);

        Ratio ratioFrom = ratioRepository
                .findByCurrencyUnit_CurrencyNameAndDateString(currencyFrom,dateString)
                .orElseThrow(() -> new CurrencyUnitNotFoundException(currencyFrom));
        Ratio ratioTo = ratioRepository
                .findByCurrencyUnit_CurrencyNameAndDateString(currencyTo,dateString)
                .orElseThrow(() -> new CurrencyUnitNotFoundException(currencyTo));


    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        return null;
    }

    @Override
    public long getCurScale(String currencyName) {
        return 0;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo) {
        return null;
    }

}
