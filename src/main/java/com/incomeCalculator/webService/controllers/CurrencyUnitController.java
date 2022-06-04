package com.incomeCalculator.webService.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.exceptions.PermissionException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.CurrencyUnitService;
import com.incomeCalculator.webService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CurrencyUnitController {

    private final CurrencyUnitRepository repository;
    private final CurrencyUnitModelAssembler assembler;
    private final CurrencyUnitService service;

    @Autowired
    private JwtTokenService tokenService;
    @Autowired
    private UserService userService;

    CurrencyUnitController(CurrencyUnitRepository repository,
                           @Qualifier("currencyUnitModelAssembler") CurrencyUnitModelAssembler assembler,
                           CurrencyUnitService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }


    @GetMapping("/currencyUnits")
    public CollectionModel<EntityModel<CurrencyUnitEntity>> all(){

        List<EntityModel<CurrencyUnitEntity>> currencyUnits = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(currencyUnits,linkTo(methodOn(CurrencyUnitController.class).all())
                .withSelfRel());
    }

    @GetMapping("/currencyUnits/{param}")
    public EntityModel<CurrencyUnitEntity> one(@PathVariable String param
            , @RequestParam(defaultValue = "0",name = "parammode") String paramMode) {
        CurrencyUnitEntity currencyUnit = service.getCurrencyUnitWithParam(param,Long.parseLong(paramMode));
        return assembler.toModel(currencyUnit);
    }

    @PostMapping("/currencyUnits")
    public EntityModel<CurrencyUnitEntity> createCurrencyUnit(@RequestBody CurrencyUnitEntity currencyUnit,
                                                              HttpServletResponse response) {

        String token = tokenService.getTokenFromResponse(response);
        User user = tokenService.getUserFromToken(token);
        if(userService.isAdmin(user)) {
            if (currencyUnit.getCurrencyName().length() > 3) {
                throw new IllegalArgumentException("Currency name must be 3 chars length");
            }
            if (!repository.findByCurrencyId(currencyUnit.getCurrencyId()).isPresent()) {
                currencyUnit = repository.save(currencyUnit);
                return assembler.toModel(currencyUnit);
            } else {
                throw new IllegalArgumentException("Currency with id " + currencyUnit.getCurrencyId() +
                        " already exists");
            }
        } else {
            throw new PermissionException();
        }
    }

    @DeleteMapping("/currencyUnits/{id}")
    public String deleteCurrencyUnit(@PathVariable Long id,
                                     HttpServletResponse response) {
        String token = tokenService.getTokenFromResponse(response);
        User user = tokenService.getUserFromToken(token);
        if(userService.isAdmin(user)) {
            CurrencyUnitEntity currencyUnit = repository.findById(id)
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(id));
            repository.delete(currencyUnit);
            return "CurrencyUnit " + currencyUnit.getCurrencyName() + " has been deleted";
        } else {
            throw new PermissionException();
        }
    }

    @PutMapping("/currencyUnits/{id}")
    public EntityModel<CurrencyUnitEntity> updateCurrencyUnit(@PathVariable Long id,
                                                              CurrencyUnitEntity currencyUnit,
                                                              HttpServletResponse response) {

        String token = tokenService.getTokenFromResponse(response);
        User user = tokenService.getUserFromToken(token);
        if(userService.isAdmin(user)) {
            CurrencyUnitEntity foundCurrencyUnit = repository.findById(id)
                    .orElseThrow(() -> new CurrencyUnitNotFoundException(id));
            currencyUnit.setId(foundCurrencyUnit.getId());
            currencyUnit = repository.save(currencyUnit);

            return assembler.toModel(currencyUnit);
        } else {
            throw new PermissionException();
        }
    }

}
