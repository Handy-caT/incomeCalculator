package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.modelAssembelrs.CardModelAssembler;
import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.TransactionModel;
import com.incomeCalculator.webService.repositories.CardRepository;
import com.incomeCalculator.webService.requests.CardRequest;
import com.incomeCalculator.webService.requests.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardRepository repository;
    private final CardModelAssembler assembler;

    public CardController(CardRepository repository, CardModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/cards")
    public CollectionModel<EntityModel<Card>> all() {
        return null;
    }

    @GetMapping("/cards/{id}")
    public EntityModel<Card> getById(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/cards/{id}/transactions")
    public CollectionModel<EntityModel<TransactionModel>> getTransactions(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/cards")
    public EntityModel<Card> createCard(@RequestBody CardRequest cardRequest) {
        return null;
    }

    @PostMapping("/cards/{id}/transactions")
    public EntityModel<TransactionModel> receiveTransaction(@PathVariable Long id,
                                                            @RequestBody TransactionRequest transactionRequest) {
        return null;
    }

    @PatchMapping("/cards/{id}")
    public EntityModel<Card> renameCard(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/cards/{id}")
    public String deleteCardById(@PathVariable Long id) {
        return null;
    }


}
