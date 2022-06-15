package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.exceptions.CardNotFoundException;
import com.incomeCalculator.webService.exceptions.PermissionException;
import com.incomeCalculator.webService.exceptions.TransactionNotFoundException;
import com.incomeCalculator.webService.exceptions.UserNotFoundException;
import com.incomeCalculator.webService.modelAssembelrs.CardModelAssembler;
import com.incomeCalculator.webService.modelAssembelrs.TransactionModelAssembler;
import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.TransactionEntity;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.CardRepository;
import com.incomeCalculator.webService.repositories.TransactionRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import com.incomeCalculator.webService.requests.CardRequest;
import com.incomeCalculator.webService.requests.TransactionRequest;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.CardService;
import com.incomeCalculator.webService.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardRepository repository;
    private final CardModelAssembler assembler;
    private final CardService service;

    @Autowired
    private JwtTokenService tokenService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionModelAssembler transactionAssembler;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public CardController(CardRepository repository, CardModelAssembler assembler, CardService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/cards")
    public CollectionModel<EntityModel<Card>> all(HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        User user = tokenService.getUserFromToken(token);
        List<EntityModel<Card>> cards;
        if(userService.isAdmin(user)) {
            cards = repository.findAll().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());


        } else {
            List<Card> list = repository.findByUser_Login(user.getLogin())
                    .orElseThrow(() -> new CardNotFoundException(user));
            cards = list.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
        }
        return CollectionModel.of(cards, linkTo(CardController.class)
                .withSelfRel());
    }

    @GetMapping("/cards/{id}")
    public EntityModel<Card> getById(@PathVariable Long id, HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(tokenService.validateUsersToken(cardUser,token)) {
            return assembler.toModel(card);
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/cards/{id}/transactions")
    public CollectionModel<EntityModel<TransactionEntity>> getTransactions(@PathVariable Long id,
                                                                           HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(tokenService.validateUsersToken(cardUser,token)) {
            List<TransactionEntity> transactionEntityList = transactionRepository.findAllByCard(card)
                    .orElseThrow(() -> new TransactionNotFoundException(card));
            List<EntityModel<TransactionEntity>> entityModelList = transactionEntityList.stream()
                    .map(transactionAssembler::toModel)
                    .collect(Collectors.toList());

            return CollectionModel.of(entityModelList,linkTo(CardController.class).slash(card.getId())
                    .slash("transactions").withSelfRel());
        } else {
            throw new PermissionException();
        }
    }

    @PostMapping("/cards")
    public EntityModel<Card> createCard(@RequestBody CardRequest cardRequest, HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        User user = tokenService.getUserFromToken(token);

        Card card = service.createCardByRequest(user,cardRequest);
        log.info("Card created: "  + card);
        return assembler.toModel(card);
    }

    @PostMapping("/cards/{id}/transactions")
    public EntityModel<TransactionEntity> receiveTransaction(@PathVariable Long id,
                                                             @RequestBody TransactionRequest transactionRequest,
                                                             HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(tokenService.validateUsersToken(cardUser,token)) {
            TransactionEntity transaction = service.executeTransaction(card,transactionRequest);
            log.info("Transaction executed: " + transaction);
            return transactionAssembler.toModel(transaction);
        } else {
            throw new PermissionException();
        }
    }

    @DeleteMapping("/cards/{id}/transactions/{transactionId}")
    public EntityModel<Card> deleteTransaction(@PathVariable Long id,
                                               @PathVariable Long transactionId,
                                               HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        Card finalCard = card;
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(finalCard.getUserName()));

        if(tokenService.validateUsersToken(cardUser,token)) {
            TransactionEntity transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException(id));
            TransactionEntity revertTransaction = (TransactionEntity) transaction.revert();
            card = service.revertTransaction(card,revertTransaction);

            transactionRepository.delete(transaction);
            log.info("Transaction deleted: " + transaction);
            return assembler.toModel(card);
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/cards/{id}/transactions/{transactionId}")
    public EntityModel<TransactionEntity> receiveTransaction(@PathVariable Long id,
                                                             @PathVariable Long transactionId,
                                                             HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(tokenService.validateUsersToken(cardUser,token)) {
            TransactionEntity transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException(id));
            Card transactionCard = repository.findByCardName(transaction.getCardName())
                    .orElseThrow(() -> new CardNotFoundException(transaction.getCardName()));
            if(transactionCard.equals(card)) {
                return transactionAssembler.toModel(transaction);
            } else {
                throw new PermissionException();
            }
        } else {
            throw new PermissionException();
        }
    }

    @PatchMapping("/cards/{id}")
    public EntityModel<Card> renameCard(@PathVariable Long id,
                                        @RequestBody String cardName,
                                        HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        Card finalCard = card;
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(finalCard.getUserName()));

        if(tokenService.validateUsersToken(cardUser,token)) {
            card.setCardName(cardName);
            card = repository.save(card);
            log.info("Card patched: " + card);
            return assembler.toModel(card);
        } else {
            throw new PermissionException();
        }
    }

    @DeleteMapping("/cards/{id}")
    public String deleteCardById(@PathVariable Long id, HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(tokenService.validateUsersToken(cardUser,token)) {
            repository.delete(card);
            log.info("Card deleted: " + card);
            return "Card " + card + " has been deleted!";
        } else {
            throw new PermissionException();
        }
    }


}
