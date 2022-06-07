package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.exceptions.PermissionException;
import com.incomeCalculator.webService.exceptions.RatioNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.modelAssembelrs.RatioModelAssembler;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import com.incomeCalculator.webService.requests.RatioRequest;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.RatioService;
import com.incomeCalculator.webService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private JwtTokenService tokenService;
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

        ratios = dateString.map(s -> repository.findAllByDateString(s).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList())).orElseGet(() -> repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()));
        return CollectionModel.of(ratios, linkTo(methodOn(RatioController.class)
                .all(Optional.of(dateStringNow)))
                .withSelfRel());
    }

    @GetMapping("/ratios/{param}")
    public EntityModel<Ratio> one(@PathVariable String param
            , @RequestParam(defaultValue = "0",name = "parammode") String paramMode) {
        Ratio ratio;
        Date date = new Date();
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
        String token = tokenService.getTokenFromRequest(request);
        User user = tokenService.getUserFromToken(token);
        if(userService.isAdmin(user)) {
            Ratio ratio = repository.findById(id)
                    .orElseThrow(() -> new RatioNotFoundException(id));
            repository.delete(ratio);

            return "Ratio " + ratio + " deleted";
        } else {
            throw new PermissionException();
        }
    }

    @PutMapping("/ratios/{id}")
    public EntityModel<Ratio> updateById(@RequestBody RatioRequest ratioRequest
            , @PathVariable Long id, HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        User user = tokenService.getUserFromToken(token);
        if(userService.isAdmin(user)) {
            Ratio ratio = repository.findById(id)
                    .orElseThrow(() -> new RatioNotFoundException(id));
            ratio = service.updateRatioByRequest(ratioRequest,ratio);
            ratio = repository.save(ratio);

            return assembler.toModel(ratio);
        } else {
            throw new PermissionException();
        }

    }

    @PostMapping("/ratios")
    public EntityModel<Ratio> addRatio(@RequestBody RatioRequest ratioRequest, HttpServletRequest request) {

        String token = tokenService.getTokenFromRequest(request);
        User user = tokenService.getUserFromToken(token);
        if(userService.isAdmin(user)) {
            Date date = new Date();
            if(repository.findByCurrencyUnit_CurrencyNameAndDateString(ratioRequest.getCurrencyName(),
                    DateFormatter.sqlFormat(date)).isPresent() ) {
                throw new IllegalArgumentException("This ratio is already exist, try update it if it's needed");
            } else {
                Ratio ratio = service.createRatioFromRequest(ratioRequest);

                ratio = repository.save(ratio);
                return assembler.toModel(ratio);
            }
        } else {
            throw new PermissionException();
        }
    }

}
