package com.incomeCalculator.cardservice.services;

import com.incomeCalculator.cardservice.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import com.incomeCalculator.cardservice.models.Ratio;
import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.cardservice.repositories.RatioRepository;
import com.incomeCalculator.cardservice.requests.RatioDto;
import com.incomeCalculator.cardservice.util.RatioBuilder;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    public Ratio createRatioFromRequest(RatioDto ratioDto) {
        Ratio ratio = new Ratio();

        ratio.setDateString(ratioDto.getDateString());
        ratio.setRatio(ratioDto.getRatio());
        CurrencyUnitEntity currencyUnit = currencyUnitRepository
                .findByCurrencyName(ratioDto.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(ratioDto.getCurrencyName()));
        ratio.setCurrencyUnit(currencyUnit);

        return ratio;
    }

    public Ratio updateRatioByRequest(RatioDto ratioDto, Ratio ratio) {

        ratio.setDateString(ratioDto.getDateString());
        ratio.setRatio(ratioDto.getRatio());
        CurrencyUnitEntity currencyUnit = currencyUnitRepository
                .findByCurrencyName(ratioDto.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(ratioDto.getCurrencyName()));
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

    @Scheduled(cron = "0 1 0 * * *")
    public void initRatios() {
        initRatios(new Date());
    }
}
