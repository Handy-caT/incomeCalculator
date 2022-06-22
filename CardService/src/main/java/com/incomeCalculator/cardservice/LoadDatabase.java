package com.incomeCalculator.cardservice;

import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.cardservice.util.CurrencyUnitEntitiesBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            if(!repository.findByCurrencyName("BYN").isPresent()) {
                log.info("Preloading " + repository
                        .save(new CurrencyUnitEntity("BYN", 1, 1)));
            }
        };
    }

}