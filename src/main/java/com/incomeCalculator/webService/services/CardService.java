package com.incomeCalculator.webService.services;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.TransactionEntity;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.CardRepository;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.TransactionRepository;
import com.incomeCalculator.webService.requests.CardRequest;
import com.incomeCalculator.webService.requests.TransactionRequest;
import com.incomeCalculator.webService.util.CurrencyUpdaterSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CardService {

    @Autowired
    private CardRepository repository;
    @Autowired
    private CurrencyUnitRepository currencyUnitRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CurrencyUpdaterSQL updater;



    public Card createCardByRequest(User user, CardRequest request) {
        CurrencyUnitEntity currencyUnit = currencyUnitRepository.findByCurrencyName(request.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(request.getCurrencyName()));

        return repository.save(new Card(currencyUnit, BigDecimal.ZERO,user,request.getCardName()));
    }

    public TransactionEntity executeTransaction(Card card, TransactionRequest request) {

        TransactionEntity transaction;
        CurrencyUnitEntity currencyUnit = currencyUnitRepository.findByCurrencyName(request.getCurrencyName())
                .orElseThrow(() -> new CurrencyUnitNotFoundException(request.getCurrencyName()));

        if(request.getAmount().signum() >= 0) {
            transaction = new TransactionEntity(currencyUnit,request.getAmount(),true);
        } else {
            transaction = new TransactionEntity(currencyUnit,request.getAmount(),false);
        }
        BigDecimal beforeBalance = card.getBalance().getAmount();
        transaction.setBeforeBalance(beforeBalance);
        transaction.setUpdater(updater);

        card.receiveTransaction(transaction);

        BigDecimal afterBalance = card.getBalance().getAmount();
        transaction.setAfterBalance(afterBalance);

        repository.save(card);
        return transactionRepository.save(transaction);
    }
    public Card revertTransaction(Card card,TransactionEntity transaction) {
        transaction.setUpdater(updater);
        card.receiveTransaction(transaction);
        return repository.save(card);
    }
}
