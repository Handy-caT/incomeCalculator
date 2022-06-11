package com.incomeCalculator.webService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.modelAssembelrs.CardModelAssembler;
import com.incomeCalculator.webService.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.modelAssembelrs.TransactionModelAssembler;
import com.incomeCalculator.webService.models.Card;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Token;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.*;
import com.incomeCalculator.webService.requests.CardRequest;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void createCardTest() throws Exception {
        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        ObjectMapper objectMapper = new ObjectMapper();
        CardRequest cardRequest = new CardRequest("newCard","USD");
        String json = objectMapper.writeValueAsString(cardRequest);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(null,currencyUnit, BigDecimal.ZERO,regularUser,cardRequest.getCardName());
        Card savedCard = new Card(1L,currencyUnit, BigDecimal.ZERO,regularUser,cardRequest.getCardName());

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(repository.save(card)).thenReturn(savedCard);

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCard.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(savedCard.getCurrencyUnit().getCurrencyName()))
                .andExpect(jsonPath("$.balance.amount").value(savedCard.getBalance().getAmount()))
                .andExpect(jsonPath("$.cardName").value(savedCard.getCardName()))
                .andDo(print());


        verify(repository,times(1)).save(card);
    }

    @Test
    public void shouldReturn404WhenCurrencyUnitNotFound() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        ObjectMapper objectMapper = new ObjectMapper();
        CardRequest cardRequest = new CardRequest("newCard","USS");
        String json = objectMapper.writeValueAsString(cardRequest);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(currencyUnitRepository.findByCurrencyName(cardRequest.getCurrencyName()))
                .thenThrow(new CurrencyUnitNotFoundException(cardRequest.getCurrencyName()));

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

   
}