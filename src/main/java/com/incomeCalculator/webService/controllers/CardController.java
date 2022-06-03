package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.modelAssembelrs.CardModelAssembler;
import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.TransactionEntity;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.CardRepository;
import com.incomeCalculator.webService.requests.CardRequest;
import com.incomeCalculator.webService.requests.TransactionRequest;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardRepository repository;
    private final CardModelAssembler assembler;
    private final CardService service;

    @Autowired
    private JwtTokenService tokenService;

    public CardController(CardRepository repository, CardModelAssembler assembler, CardService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/cards")
    public CollectionModel<EntityModel<Card>> all() {
        return null;
    }

    @GetMapping("/cards/{id}")
    public EntityModel<Card> getById(@PathVariable Long id, HttpServletResponse response) {
        String token = tokenService.getTokenFromResponse(response);
        User user = tokenService.getUserFromToken(token);

        if(tokenService.validateUsersToken(user,token)) {


            return null;
        }

        return null;
    }

    @GetMapping("/cards/{id}/transactions")
    public CollectionModel<EntityModel<TransactionEntity>> getTransactions(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/cards")
    public EntityModel<Card> createCard(@RequestBody CardRequest cardRequest, HttpServletResponse response) {
        String token = tokenService.getTokenFromResponse(response);
        User user = tokenService.getUserFromToken(token);

        Card card = service.createCardByRequest(user,cardRequest);
        return assembler.toModel(card);
    }

    @PostMapping("/cards/{id}/transactions")
    public EntityModel<TransactionEntity> receiveTransaction(@PathVariable Long id,
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
