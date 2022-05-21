package com.incomeCalculator.webService;

import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.models.Role;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import com.incomeCalculator.webService.repositories.RoleRepository;
import com.incomeCalculator.webService.util.CurrencyUnitEntitiesBuilder;
import com.incomeCalculator.webService.util.RatioBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Date;
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

    @Bean
    CommandLineRunner initRatio(RatioRepository repository,CurrencyUnitRepository currencies) {

        Date date = new Date();
        String dateString = DateFormatter.sqlFormat(date);

        if(repository.findAllByDateString(dateString).get().isEmpty()) {
            return args -> {
                RatioBuilder builder = new RatioBuilder(repository,currencies,dateString);
                List<String> namesList = builder.getBuildPlan();
                for(String currencyName : namesList) {
                    builder.buildCurrency(currencyName);
                }
            };
        } else return null;
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            log.info("Preloading " + roleRepository.save(new Role("ROLE_USER")));
            log.info("Preloading " + roleRepository.save(new Role("ROLE_ADMIN")));
        };
    }
}