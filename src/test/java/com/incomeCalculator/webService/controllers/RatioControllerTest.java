package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.models.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.models.modelAssembelrs.RatioModelAssembler;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.security.JwtFilter;
import com.incomeCalculator.webService.services.CurrencyUnitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatioController.class)
@AutoConfigureMockMvc(addFilters = false)
class RatioControllerTest {

    static SecureRandom random = new SecureRandom();

    @MockBean
    RatioRepository repository;
    @MockBean
    RatioModelAssembler assembler;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    JwtFilter filter;

    @Autowired
    MockMvc mockMvc;

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

        return value;
    }

    @Test
    public void getTestWithoutParammode() throws Exception {
        Long id = 1L;
        BigDecimal ratioAmount = randomValue();
        Date date = new Date();
        String dateString = DateFormatter.sqlFormat(date);
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Ratio ratio = new Ratio(id,currencyUnit,ratioAmount,dateString);
        EntityModel<Ratio> ratioModel = EntityModel.of(ratio,
                linkTo(methodOn(RatioController.class).one(ratio.getId().toString(),"0")).withSelfRel(),
                linkTo(methodOn(RatioController.class).all(Optional.of(ratio.getDateString())))
                        .withRel("ratios"),
                linkTo(methodOn(CurrencyUnitController.class)
                        .one(ratio.getCurrencyUnit().getId().toString(),"0")).withRel("currencyUnit"));

        when(repository.findById(id)).thenReturn(Optional.of(ratio));
        when(assembler.toModel(ratio)).thenReturn(ratioModel);

        mockMvc.perform(get("/ratios/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.ratio").value(ratioAmount))
                .andExpect(jsonPath("$.dateString").value(dateString))
                .andExpect(jsonPath("$.currencyUnit.id").value(currencyUnit.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyUnit.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyUnit.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(linkTo(methodOn(RatioController.class)
                                .one(ratio.getId().toString(),"0")).toString()))
                .andExpect(jsonPath("$._links.currencyUnit.href")
                        .value(linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andExpect(jsonPath("$._links.ratios.href")
                        .value(linkTo(methodOn(RatioController.class)
                                .all(Optional.of(ratio.getDateString()))).toString()))
                .andDo(print());
    }

}