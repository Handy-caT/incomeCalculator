package com.incomeCalculator.webService;

import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CurrencyUnitRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new CurrencyUnitEntity("USD", 12, 1)));
            log.info("Preloading " + repository.save(new CurrencyUnitEntity("EUR", 13, 1)));
        };
    }
}