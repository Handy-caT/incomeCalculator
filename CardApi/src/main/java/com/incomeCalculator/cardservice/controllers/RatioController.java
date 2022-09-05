package com.incomeCalculator.cardservice.controllers;


import com.incomeCalculator.cardservice.exceptions.RatioNotFoundException;
import com.incomeCalculator.cardservice.modelAssemblers.RatioModelAssembler;
import com.incomeCalculator.cardservice.models.Ratio;
import com.incomeCalculator.cardservice.repositories.RatioRepository;
import com.incomeCalculator.cardservice.requests.RatioDto;
import com.incomeCalculator.cardservice.services.RatioService;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RatioController {

    private final RatioRepository repository;
    private final RatioModelAssembler assembler;
    private final RatioService service;

    @Autowired
    private RequestHandler handler;
    @Autowired
    private UserService userService;


    public RatioController(RatioRepository repository, RatioModelAssembler assembler, RatioService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/ratios")
    public CollectionModel<EntityModel<Ratio>> all(
            @RequestParam(required = false,name = "ondate") Optional<String> dateString) {
        List<EntityModel<Ratio>> ratios;

        Date date = new Date();
        String dateStringNow = DateFormatter.sqlFormat(date);

        if(dateString.isPresent()) {
            Date requestDate;
            try {
                requestDate = DateFormatter.sqlParse(dateString.get());
            } catch (ParseException e) {
                throw new IllegalArgumentException("Bad date format");
            }
            if (repository.findAllByDateString(dateString.get()).isEmpty()) {
                service.initRatios(requestDate);
            }
        }

        ratios = dateString.map(s -> repository.findAllByDateString(s).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList())).orElseGet(() -> repository.findAllByDateString(dateStringNow)
                .stream().map(assembler::toModel)
                .collect(Collectors.toList()));

        if(dateString.isPresent()) {
            return CollectionModel.of(ratios, linkTo(methodOn(RatioController.class)
                    .all(dateString))
                    .withSelfRel());
        } else {
            return CollectionModel.of(ratios, linkTo(methodOn(RatioController.class)
                    .all(Optional.of(dateStringNow)))
                    .withSelfRel());
        }
    }

    @GetMapping("/ratios/{param}")
    public EntityModel<Ratio> one(@PathVariable String param
            , @RequestParam(defaultValue = "0",name = "parammode") String paramMode) {
        Ratio ratio;
        Date date = new Date();

        if (repository.findAllByDateString(DateFormatter.sqlFormat(date)).isEmpty()) {
            service.initRatios(date);
        }

        if(Objects.equals(paramMode, "1")) {
            ratio = repository
                    .findByCurrencyUnit_CurrencyNameAndDateString(param, DateFormatter.sqlFormat(date))
                    .orElseThrow(() -> new RatioNotFoundException(param));
        } else {
            ratio = repository.findById(Long.parseLong(param))
                    .orElseThrow(() -> new RatioNotFoundException(Long.parseLong(param)));
        }
        return assembler.toModel(ratio);
    }

    @DeleteMapping("/ratios/{id}")
    public String deleteById(@PathVariable Long id, HttpServletRequest request) {
        User authUser = handler.getUserFromRequest(request);
        if(userService.isAdmin(authUser)) {
            Ratio ratio = repository.findById(id)
                    .orElseThrow(() -> new RatioNotFoundException(id));
            repository.delete(ratio);

            return "Ratio " + ratio + " deleted";
        } else {
            throw new PermissionException();
        }
    }

    @DeleteMapping("/ratios")
    public String deleteByDate(@RequestParam(name = "ondate") String dateString, HttpServletRequest request) {
        User authUser = handler.getUserFromRequest(request);
        if(userService.isAdmin(authUser)) {

            repository.deleteAllByDateString(dateString);
            return "Ratios deleted";
        } else {
            throw new PermissionException();
        }
    }

    @PutMapping("/ratios/{id}")
    public EntityModel<Ratio> updateById(@RequestBody RatioDto ratioDto
            , @PathVariable Long id, HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        if(userService.isAdmin(authUser)) {
            Ratio ratio = repository.findById(id)
                    .orElseThrow(() -> new RatioNotFoundException(id));
            ratio = service.updateRatioByRequest(ratioDto,ratio);
            ratio = repository.save(ratio);

            return assembler.toModel(ratio);
        } else {
            throw new PermissionException();
        }

    }

    @PostMapping("/ratios")
    public EntityModel<Ratio> addRatio(@RequestBody RatioDto ratioDto, HttpServletRequest request) {

        User authUser = handler.getUserFromRequest(request);
        if(userService.isAdmin(authUser)) {
            Date date = new Date();
            if(repository.findByCurrencyUnit_CurrencyNameAndDateString(ratioDto.getCurrencyName(),
                    DateFormatter.sqlFormat(date)).isPresent() ) {
                throw new IllegalArgumentException("This ratio is already exist, try update it if it's needed");
            } else {
                Ratio ratio = service.createRatioFromRequest(ratioDto);

                repository.save(ratio);
                return assembler.toModel(ratio);
            }
        } else {
            throw new PermissionException();
        }
    }

}
