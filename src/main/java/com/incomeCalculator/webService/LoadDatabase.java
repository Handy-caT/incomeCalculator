package com.incomeCalculator.webService;

import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import com.incomeCalculator.webService.util.CurrencyUnitEntitiesBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initCurrencies(CurrencyUnitRepository repository) {

        CurrencyUnitEntitiesBuilder builder = new CurrencyUnitEntitiesBuilder(repository);

        List<String> namesList = builder.getBuildPlan();

        return args -> {
            for(String currencyName : namesList) {
                if(!repository.findByCurrencyName(currencyName).isPresent()) {
                    builder.buildCurrencyUnit(currencyName);
                }
            }
        };
    }

    //@Bean
    CommandLineRunner initRatio(RatioRepository repository,CurrencyUnitRepository currencies) {
        return args -> {
            log.info("Preloading " + repository.save(new Ratio(currencies.findByCurrencyName("USD").get(), BigDecimal.ONE)));
            log.info("Preloading " + repository.save(new Ratio(currencies.findByCurrencyName("EUR").get(), BigDecimal.ONE)));
        };
    }
}