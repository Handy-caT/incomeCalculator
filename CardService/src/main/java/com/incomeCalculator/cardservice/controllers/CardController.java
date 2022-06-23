package com.incomeCalculator.cardservice.controllers;

import com.incomeCalculator.cardservice.exceptions.CardNotFoundException;
import com.incomeCalculator.cardservice.exceptions.TransactionNotFoundException;
import com.incomeCalculator.cardservice.modelAssemblers.CardModelAssembler;
import com.incomeCalculator.cardservice.modelAssemblers.TransactionModelAssembler;
import com.incomeCalculator.cardservice.models.Card;
import com.incomeCalculator.cardservice.models.TransactionEntity;
import com.incomeCalculator.cardservice.repositories.CardRepository;
import com.incomeCalculator.cardservice.repositories.TransactionRepository;
import com.incomeCalculator.cardservice.requests.CardRequest;
import com.incomeCalculator.cardservice.requests.TransactionRequest;
import com.incomeCalculator.cardservice.services.CardService;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.repositories.UserRepository;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@ComponentScan(basePackages = {"com.incomeCalculator.userservice.services"})
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardRepository repository;
    private final CardModelAssembler assembler;
    private final CardService service;

    @Autowired
    private RequestHandler handler;
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

        User user = handler.getUserFromRequest(request);
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
        User authUser = handler.getUserFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(userService.validateUser(cardUser.getId(),authUser)) {
            return assembler.toModel(card);
        } else {
            throw new PermissionException();
        }
    }

    @GetMapping("/cards/{id}/transactions")
    public CollectionModel<EntityModel<TransactionEntity>> getTransactions(@PathVariable Long id,
                                                                           HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(userService.validateUser(cardUser.getId(),authUser)) {
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
        User authUser = handler.getUserFromRequest(request);

        Card card = service.createCardByRequest(authUser,cardRequest);
        log.info("Card created: "  + card);
        return assembler.toModel(card);
    }

    @PostMapping("/cards/{id}/transactions")
    public EntityModel<TransactionEntity> receiveTransaction(@PathVariable Long id,
                                                             @RequestBody TransactionRequest transactionRequest,
                                                             HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(userService.validateUser(cardUser.getId(),authUser)) {
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

        User authUser = handler.getUserFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        Card finalCard = card;
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(finalCard.getUserName()));

        if(userService.validateUser(cardUser.getId(),authUser)) {
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

        User authUser = handler.getUserFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(userService.validateUser(cardUser.getId(),authUser)) {
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

        User authUser = handler.getUserFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        Card finalCard = card;
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(finalCard.getUserName()));

        if(userService.validateUser(cardUser.getId(),authUser)) {
            card.setCardName(cardName);
            repository.save(card);
            log.info("Card patched: " + card);
            return assembler.toModel(card);
        } else {
            throw new PermissionException();
        }
    }

    @DeleteMapping("/cards/{id}")
    public String deleteCardById(@PathVariable Long id, HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        Card card = repository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User cardUser = userRepository.findByLogin(card.getUserName())
                .orElseThrow(() -> new UserNotFoundException(card.getUserName()));

        if(userService.validateUser(cardUser.getId(),authUser)) {
            repository.delete(card);
            log.info("Card deleted: " + card);
            return "Card " + card + " has been deleted!";
        } else {
            throw new PermissionException();
        }
    }


}
