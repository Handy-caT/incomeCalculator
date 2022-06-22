package com.incomeCalculator.cardservice.services;

import com.incomeCalculator.cardservice.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.cardservice.models.Card;
import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import com.incomeCalculator.cardservice.models.TransactionEntity;
import com.incomeCalculator.cardservice.repositories.CardRepository;
import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.cardservice.repositories.TransactionRepository;
import com.incomeCalculator.cardservice.requests.CardRequest;
import com.incomeCalculator.cardservice.requests.TransactionRequest;
import com.incomeCalculator.cardservice.util.CurrencyUpdaterSQL;
import com.incomeCalculator.userservice.models.User;
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
        Card card = new Card(currencyUnit, BigDecimal.ZERO,user,request.getCardName());
        card = repository.save(card);
        return card;
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
