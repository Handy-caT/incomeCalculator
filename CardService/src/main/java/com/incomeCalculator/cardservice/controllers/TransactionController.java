package com.incomeCalculator.cardservice.controllers;

import com.incomeCalculator.webService.exceptions.TransactionNotFoundException;
import com.incomeCalculator.webService.modelAssembelrs.TransactionModelAssembler;
import com.incomeCalculator.webService.models.TransactionEntity;
import com.incomeCalculator.webService.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionRepository repository;
    private final TransactionModelAssembler assembler;

    public TransactionController(TransactionRepository repository, TransactionModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/transactions")
    public CollectionModel<EntityModel<TransactionEntity>> all() {
        List<EntityModel<TransactionEntity>> transactions = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions,linkTo(methodOn(TransactionController.class).all())
                .withSelfRel());
    }

    @GetMapping("/transactions/{id}")
    public EntityModel<TransactionEntity> getById(@PathVariable Long id) {
        TransactionEntity transaction = repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        return assembler.toModel(transaction);
    }

}
