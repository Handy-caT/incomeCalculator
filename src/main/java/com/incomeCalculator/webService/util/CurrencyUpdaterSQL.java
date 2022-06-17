package com.incomeCalculator.webService.util;

import com.incomeCalculator.core.wallet.money.CurrencyConverter;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.exceptions.RatioNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class CurrencyUpdaterSQL implements CurrencyUpdater, CurrencyUpdaterFactory {

    @Autowired
    private RatioRepository ratioRepository;
    @Autowired
    private CurrencyUnitRepository currencyUnitRepository;



    private BigDecimal getRatioOnDate(String currencyFrom, String currencyTo,String dateString) {
        Ratio ratioFrom = ratioRepository
                .findByCurrencyUnit_CurrencyNameAndDateString(currencyFrom, dateString)
                .orElseThrow(() -> new RatioNotFoundException(currencyFrom));
        Ratio ratioTo = ratioRepository
                .findByCurrencyUnit_CurrencyNameAndDateString(currencyTo, dateString)
                .orElseThrow(() -> new RatioNotFoundException(currencyTo));

        CurrencyUnitEntity currencyUnitFrom = currencyUnitRepository.findByCurrencyName(currencyFrom)
                .orElseThrow(() -> new CurrencyUnitNotFoundException(currencyFrom));
        CurrencyUnitEntity currencyUnitTo = currencyUnitRepository.findByCurrencyName(currencyTo)
                .orElseThrow(() -> new CurrencyUnitNotFoundException(currencyTo));

        BigDecimal ratio = ratioFrom.getRatio().divide(ratioTo.getRatio() ,10,RoundingMode.HALF_UP);
        ratio = ratio.setScale((int) (ratio.scale() + Math.log10(currencyUnitFrom.getCurrencyScale())),RoundingMode.HALF_DOWN)
                .divide(BigDecimal.valueOf(currencyUnitFrom.getCurrencyScale()),RoundingMode.HALF_DOWN);
        ratio = ratio.setScale((int) (ratio.scale() + Math.log10(currencyUnitTo.getCurrencyScale())),RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(currencyUnitTo.getCurrencyScale()));

        return ratio;
    }

    @Override
    public CurrencyUpdater createUpdater() {
        return this;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        Date date = new Date();
        String dateString = DateFormatter.sqlFormat(date);

        if(Objects.equals(currencyFrom,currencyTo)) {
            return BigDecimal.ONE;
        } else {
            return getRatioOnDate(currencyFrom,currencyTo,dateString);
        }
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        String dateString = DateFormatter.sqlFormat(date);

        if(Objects.equals(currencyFrom,currencyTo)) {
            return BigDecimal.ONE;
        } else {
            return getRatioOnDate(currencyFrom,currencyTo,dateString);
        }
    }

    @Override
    public long getCurScale(String currencyName) {
        CurrencyUnitEntity currencyUnitFrom = currencyUnitRepository.findByCurrencyName(currencyName)
                .orElseThrow(() -> new CurrencyUnitNotFoundException(currencyName));

        return currencyUnitFrom.getCurrencyScale();
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyToList) {
        Date date = new Date();
        String dateString = DateFormatter.sqlFormat(date);

        Map<String,BigDecimal> currenciesHash = new HashMap<>();
        for(String currencyTo : currencyToList) {
            currenciesHash.put(currencyTo,getRatioOnDate(currencyFrom,currencyTo,dateString));
        }
        return currenciesHash;
    }

}
