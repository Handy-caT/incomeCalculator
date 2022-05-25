package com.incomeCalculator.webService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

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
        assertNotNull(mockMvc);
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
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andDo(print());
    }

}