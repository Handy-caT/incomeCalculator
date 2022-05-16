package com.incomeCalculator.webService;

import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initCurrencies(CurrencyUnitRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new CurrencyUnitEntity("USD", 12, 1)));
            log.info("Preloading " + repository.save(new CurrencyUnitEntity("EUR", 13, 1)));
        };
    }

    @Bean
    CommandLineRunner initRatio(RatioRepository repository,CurrencyUnitRepository currencies) {
        return args -> {
            log.info("Preloading " + repository.save(new Ratio(currencies.findByCurrencyName("USD").get(), BigDecimal.ONE)));
            log.info("Preloading " + repository.save(new Ratio(currencies.findByCurrencyName("EUR").get(), BigDecimal.ONE)));
        };
    }
}