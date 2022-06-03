package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.CardRepository;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.requests.CardRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CardService {

    private final CardRepository repository;
    private final CurrencyUnitRepository currencyUnitRepository;

    public CardService(CardRepository repository, CurrencyUnitRepository currencyUnitRepository) {
        this.repository = repository;
        this.currencyUnitRepository = currencyUnitRepository;
    }

    public Card createCardByRequest(User user, CardRequest request) {
        CurrencyUnitEntity currencyUnit = currencyUnitRepository.findByCurrencyName(request.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(request.getCurrencyName()));

        return repository.save(new Card(currencyUnit, BigDecimal.ZERO,user,request.getCardName()));
    }

}
