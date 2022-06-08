package com.incomeCalculator.webService.controllers;

import com.incomeCalculator.webService.modelAssembelrs.CardModelAssembler;
import com.incomeCalculator.webService.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.modelAssembelrs.TransactionModelAssembler;
import com.incomeCalculator.webService.repositories.*;
import com.incomeCalculator.webService.security.JwtFilter;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.CardService;
import com.incomeCalculator.webService.services.UserService;
import com.incomeCalculator.webService.util.CurrencyUpdaterSQL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {

    @MockBean
    CardRepository repository;
    @MockBean
    TransactionRepository transactionRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    JwtFilter filter;
    @MockBean
    CurrencyUnitRepository currencyUnitRepository;
    @MockBean
    RatioRepository ratioRepository;


    private String bearer = "Bearer ";

    @TestConfiguration
    static class AdditionalConfig {

        @Bean
        public CardModelAssembler getCardModelAssembler() {
            return new CardModelAssembler();
        }

        @Bean
        public CardService getCardService() {
            return new CardService();
        }

        @Bean
        public JwtTokenService getJwtTokenService() {
            return new JwtTokenService();
        }

        @Bean
        public UserService getUserService() {
            return new UserService();
        }

        @Bean
        public TransactionModelAssembler getTransactionModelAssembler() {
            return new TransactionModelAssembler();
        }

        @Bean
        public CurrencyUpdaterSQL getCurrencyUpdaterSQL() {
            return new CurrencyUpdaterSQL();
        }
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    public void ok() {

    }

}