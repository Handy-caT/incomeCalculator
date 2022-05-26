package com.incomeCalculator.webService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.security.JwtFilter;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.CurrencyUnitService;
import com.incomeCalculator.webService.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.SecurityConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CurrencyUnitController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CurrencyUnitControllerTest {

    @MockBean
    CurrencyUnitRepository repository;
    @MockBean
    CurrencyUnitModelAssembler assembler;
    @MockBean
    CurrencyUnitService service;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    JwtFilter filter;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void getTestWithParammode0() throws Exception {
        Long parammode = 0L;

        Long id = 1L;
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,"USD",432,1);
        EntityModel<CurrencyUnitEntity> model = EntityModel.of(currencyUnit,
                linkTo(methodOn(CurrencyUnitController.class).one(currencyUnit.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits"));
        when(repository.findById(id)).thenReturn(Optional.of(currencyUnit));
        when(service.getCurrencyUnitWithParam(id.toString(), parammode)).thenReturn(currencyUnit);
        when(assembler.toModel(currencyUnit)).thenReturn(model);

        mockMvc.perform(get("/currencyUnits/{param}?parammode={mode}",id,parammode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andDo(print());
    }

    @Test
    public void getTestWithParammode1() throws Exception {
        Long parammode = 1L;

        Long id = 1L;
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,"USD",432,1);
        EntityModel<CurrencyUnitEntity> model = EntityModel.of(currencyUnit,
                linkTo(methodOn(CurrencyUnitController.class).one(currencyUnit.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits"));
        when(repository.findByCurrencyName(currencyUnit.getCurrencyName())).thenReturn(Optional.of(currencyUnit));
        when(service.getCurrencyUnitWithParam(currencyUnit.getCurrencyName(), parammode)).thenReturn(currencyUnit);
        when(assembler.toModel(currencyUnit)).thenReturn(model);

        mockMvc.perform(get("/currencyUnits/{param}?parammode={mode}"
                        ,currencyUnit.getCurrencyName(),parammode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andDo(print());
    }

    @Test
    public void getTestWithParammode2() throws Exception {
        Long parammode = 2L;

        Long id = 1L;
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,"USD",432,1);
        EntityModel<CurrencyUnitEntity> model = EntityModel.of(currencyUnit,
                linkTo(methodOn(CurrencyUnitController.class).one(currencyUnit.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits"));
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.of(currencyUnit));
        when(service.getCurrencyUnitWithParam(String.valueOf(currencyUnit.getCurrencyId()), parammode)).thenReturn(currencyUnit);
        when(assembler.toModel(currencyUnit)).thenReturn(model);

        mockMvc.perform(get("/currencyUnits/{param}?parammode={mode}"
                        ,currencyUnit.getCurrencyId(),parammode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andDo(print());
    }

    @Test
    public void shouldReturn404WhenNotFound() throws Exception {
        Long parammode = 0L;

        String id = "333";
        when(service.getCurrencyUnitWithParam(id,parammode)).thenThrow(new CurrencyUnitNotFoundException(id));

        mockMvc.perform(get("/currencyUnits/{param}?parammode={mode}",id,parammode))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturnAll() throws Exception {

        CurrencyUnitEntity usdUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        CurrencyUnitEntity eurUnit = new CurrencyUnitEntity(2L,"EUR",433,1);
        CurrencyUnitEntity bynUnit = new CurrencyUnitEntity(3L,"BYN",434,1);

        List<CurrencyUnitEntity> currenciesList = new LinkedList<>();
        currenciesList.add(usdUnit);
        currenciesList.add(eurUnit);
        currenciesList.add(bynUnit);

        when(repository.findAll()).thenReturn(currenciesList);

        when(assembler.toModel(usdUnit)).thenReturn(EntityModel.of(usdUnit,
                linkTo(methodOn(CurrencyUnitController.class).one(usdUnit.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits")));
        when(assembler.toModel(eurUnit)).thenReturn(EntityModel.of(eurUnit,
                linkTo(methodOn(CurrencyUnitController.class).one(eurUnit.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits")));
        when(assembler.toModel(bynUnit)).thenReturn(EntityModel.of(bynUnit,
                linkTo(methodOn(CurrencyUnitController.class).one(bynUnit.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits")));


        mockMvc.perform(get("/currencyUnits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyUnitEntityList.size()").value(currenciesList.size()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andDo(print());
    }

}