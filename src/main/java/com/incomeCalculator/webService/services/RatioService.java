package com.incomeCalculator.webService.services;

import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.requests.RatioRequest;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RatioService {

    @Autowired
    CurrencyUnitRepository currencyUnitRepository;

    public Ratio createRatioFromRequest(RatioRequest ratioRequest) {
        Date date = new Date();
        Ratio ratio = new Ratio();

        ratio.setDateString(DateFormatter.sqlFormat(date));
        ratio.setRatio(ratioRequest.getRatio());
        CurrencyUnitEntity currencyUnit = currencyUnitRepository
                .findByCurrencyName(ratioRequest.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(ratioRequest.getCurrencyName()));
        ratio.setCurrencyUnit(currencyUnit);

        return ratio;
    }

    public Ratio updateRatioByRequest(RatioRequest ratioRequest,Ratio ratio) {
        Date date = new Date();

        ratio.setDateString(DateFormatter.sqlFormat(date));
        ratio.setRatio(ratioRequest.getRatio());
        CurrencyUnitEntity currencyUnit = currencyUnitRepository
                .findByCurrencyName(ratioRequest.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(ratioRequest.getCurrencyName()));
        ratio.setCurrencyUnit(currencyUnit);

        return ratio;
    }

}
