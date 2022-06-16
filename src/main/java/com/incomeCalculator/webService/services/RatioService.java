package com.incomeCalculator.webService.services;

import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import com.incomeCalculator.webService.requests.RatioRequest;
import com.incomeCalculator.webService.util.RatioBuilder;
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class RatioService {

    private static final Logger log = LoggerFactory.getLogger(RatioService.class);

    @Autowired
    CurrencyUnitRepository currencyUnitRepository;
    @Autowired
    RatioRepository repository;

    public Ratio createRatioFromRequest(RatioRequest ratioRequest) {
        Ratio ratio = new Ratio();

        ratio.setDateString(ratioRequest.getDateString());
        ratio.setId(ratioRequest.getId());
        ratio.setRatio(ratioRequest.getRatio());
        CurrencyUnitEntity currencyUnit = currencyUnitRepository
                .findByCurrencyName(ratioRequest.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(ratioRequest.getCurrencyName()));
        ratio.setCurrencyUnit(currencyUnit);

        return ratio;
    }

    public Ratio updateRatioByRequest(RatioRequest ratioRequest,Ratio ratio) {

        ratio.setDateString(ratioRequest.getDateString());
        ratio.setRatio(ratioRequest.getRatio());
        CurrencyUnitEntity currencyUnit = currencyUnitRepository
                .findByCurrencyName(ratioRequest.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(ratioRequest.getCurrencyName()));
        ratio.setCurrencyUnit(currencyUnit);

        return ratio;
    }

    public void initRatios(Date date) {
        RatioBuilder builder = new RatioBuilder(repository, currencyUnitRepository, date);
        List<String> namesList = builder.getBuildPlan();

        for (String currencyName : namesList) {
            builder.buildCurrency(currencyName);
        }
        log.info("Preloading " + repository
                .save(new Ratio(currencyUnitRepository.findByCurrencyName("BYN").get(),
                        BigDecimal.ONE, DateFormatter.sqlFormat(date))));

    }
}
