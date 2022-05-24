package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.security.JwtFilter;
import com.incomeCalculator.webService.services.CurrencyUnitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;


import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(CurrencyUnitController.class)
public class CurrencyUnitControllerTest {

    @MockBean
    CurrencyUnitRepository repository;
    @MockBean
    CurrencyUnitModelAssembler assembler;
    @MockBean
    JwtFilter filter;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    CurrencyUnitService service;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getCurrencyUnitById() throws Exception {
        Long id = 1L;
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,"USD",432,1);
        EntityModel<CurrencyUnitEntity> model = EntityModel.of(currencyUnit,
                linkTo(methodOn(CurrencyUnitController.class).one(currencyUnit.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(CurrencyUnitController.class).all()).withRel("currencyUnits"));
        when(repository.findById(id)).thenReturn(Optional.of(currencyUnit));
        when(service.getCurrencyUnitWithParam(id.toString(), 0L)).thenReturn(currencyUnit);
        when(assembler.toModel(currencyUnit)).thenReturn(model);

        mockMvc.perform(get("/currencyUnits/{param}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andDo(print());

    }
}