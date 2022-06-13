package com.incomeCalculator.webService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.webService.exceptions.CardNotFoundException;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.modelAssembelrs.CardModelAssembler;
import com.incomeCalculator.webService.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.modelAssembelrs.TransactionModelAssembler;
import com.incomeCalculator.webService.models.*;
import com.incomeCalculator.webService.repositories.*;
import com.incomeCalculator.webService.requests.CardRequest;
import com.incomeCalculator.webService.requests.TransactionRequest;
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
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @MockBean
    CurrencyUpdaterSQL updater;

    private Random random = new SecureRandom();

    private String bearer = "Bearer ";

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

        return value;
    }


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
                .andExpect(jsonPath("$.currencyUnit.currencyName")
                        .value(savedCard.getCurrencyUnit().getCurrencyName()))
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

   @Test
    public void validUserCanGetHisCard() throws Exception {

       User regularUser = AuthControllerTest.getRawUser();
       Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

       CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
       Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), regularUser,"cardName");

       when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
       when(tokenRepository.findByUser(regularUser)).thenReturn(Optional.of(tokenEntity));
       when(repository.findById(card.getId())).thenReturn(Optional.of(card));
       when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));

       mockMvc.perform(get("/cards/{id}",card.getId())
               .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(card.getId()))
               .andExpect(jsonPath("$.currencyUnit.currencyName")
                       .value(card.getCurrencyUnit().getCurrencyName()))
               .andExpect(jsonPath("$.balance.amount").value(card.getBalance().getAmount()))
               .andExpect(jsonPath("$.cardName").value(card.getCardName()))
               .andDo(print());

   }

    @Test
    public void shouldReturn404WhenCardNotFound() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), regularUser,"cardName");

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(card.getId())).thenThrow(new CardNotFoundException(card.getId()));

        mockMvc.perform(get("/cards/{id}",card.getId())
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowInvalidUserGetCard() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        User cardUser = AuthControllerTest.getRawUser();
        cardUser.setLogin("cardUser");
        cardUser.setId(2L);
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);
        Token cardToken = AuthControllerTest.createTokenForUser(cardUser);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), cardUser,"cardName");

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(tokenRepository.findByUser(cardUser)).thenReturn(Optional.of(cardToken));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(cardUser.getLogin())).thenReturn(Optional.of(cardUser));

        mockMvc.perform(get("/cards/{id}",card.getId())
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldAllowAdminGetEveryCard() throws Exception {

        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        User cardUser = AuthControllerTest.getRawUser();
        Token cardToken = AuthControllerTest.createTokenForUser(cardUser);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), cardUser,"cardName");

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(tokenRepository.findByUser(cardUser)).thenReturn(Optional.of(cardToken));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(cardUser.getLogin())).thenReturn(Optional.of(cardUser));

        mockMvc.perform(get("/cards/{id}",card.getId())
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(card.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName")
                        .value(card.getCurrencyUnit().getCurrencyName()))
                .andExpect(jsonPath("$.balance.amount").value(card.getBalance().getAmount()))
                .andExpect(jsonPath("$.cardName").value(card.getCardName()))
                .andDo(print());

    }

    @Test
    public void shouldPostAddTransactionForValidUser() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit,randomValue(), regularUser,"cardName");

        ObjectMapper mapper = new ObjectMapper();
        TransactionRequest request = new TransactionRequest("USD",randomValue());
        String json = mapper.writeValueAsString(request);

        TransactionEntity transaction = new TransactionEntity(currencyUnit,request.getAmount(),true);
        transaction.setUpdater(updater);
        transaction.setCard(card);
        transaction.setBeforeBalance(card.getBalance().getAmount());
        transaction.setAfterBalance(card.getBalance().getAmount().add(request.getAmount()));

        TransactionEntity savedTransaction = new TransactionEntity(1L,currencyUnit,request.getAmount(),true);
        savedTransaction.setUpdater(updater);
        savedTransaction.setCard(card);
        savedTransaction.setBeforeBalance(card.getBalance().getAmount());
        savedTransaction.setAfterBalance(card.getBalance().getAmount().add(request.getAmount()));

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(tokenRepository.findByUser(regularUser)).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);

        mockMvc.perform(post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTransaction.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.transactionAmount.amount").value(request.getAmount().doubleValue()))
                .andExpect(jsonPath("$.beforeBalance").value(transaction.getBeforeBalance().doubleValue()))
                .andExpect(jsonPath("$.afterBalance").value(transaction.getAfterBalance().doubleValue()))
                .andExpect(jsonPath("$.cardName").value(card.getCardName()))
                .andDo(print());

    }

    @Test
    public void shouldPostSubtractTransactionForValidUser() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit,randomValue(), regularUser,"cardName");

        ObjectMapper mapper = new ObjectMapper();
        TransactionRequest request = new TransactionRequest("USD",randomValue().negate());
        String json = mapper.writeValueAsString(request);

        TransactionEntity transaction = new TransactionEntity(currencyUnit,request.getAmount(),false);
        transaction.setUpdater(updater);
        transaction.setCard(card);
        transaction.setBeforeBalance(card.getBalance().getAmount());
        transaction.setAfterBalance(card.getBalance().getAmount().subtract(request.getAmount()));

        TransactionEntity savedTransaction = new TransactionEntity(1L,currencyUnit,request.getAmount(),false);
        savedTransaction.setUpdater(updater);
        savedTransaction.setCard(card);
        savedTransaction.setBeforeBalance(card.getBalance().getAmount());
        savedTransaction.setAfterBalance(card.getBalance().getAmount().subtract(request.getAmount()));

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(tokenRepository.findByUser(regularUser)).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);

        mockMvc.perform(post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTransaction.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.transactionAmount.amount").value(request.getAmount().doubleValue()))
                .andExpect(jsonPath("$.beforeBalance").value(transaction.getBeforeBalance().doubleValue()))
                .andExpect(jsonPath("$.afterBalance").value(transaction.getAfterBalance().doubleValue()))
                .andExpect(jsonPath("$.cardName").value(card.getCardName()))
                .andDo(print());

    }

    @Test
    public void shouldPostAddTransactionInOtherCurrency() throws Exception {
        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        CurrencyUnitEntity eurCurrencyUnit = new CurrencyUnitEntity(2L,"EUR",433,1);
        Card card = new Card(1L,currencyUnit,randomValue(), regularUser,"cardName");

        ObjectMapper mapper = new ObjectMapper();
        TransactionRequest request = new TransactionRequest("EUR",randomValue());
        String json = mapper.writeValueAsString(request);

        BigDecimal ratio = BigDecimal.valueOf(1.5);

        TransactionEntity transaction = new TransactionEntity(eurCurrencyUnit,request.getAmount(),true);
        transaction.setUpdater(updater);
        transaction.setCard(card);
        transaction.setBeforeBalance(card.getBalance().getAmount());
        transaction.setAfterBalance(card.getBalance().getAmount().add(request.getAmount().multiply(ratio)));

        TransactionEntity savedTransaction = new TransactionEntity(1L,eurCurrencyUnit,request.getAmount(),true);
        savedTransaction.setUpdater(updater);
        savedTransaction.setCard(card);
        savedTransaction.setBeforeBalance(card.getBalance().getAmount());
        savedTransaction.setAfterBalance(card.getBalance().getAmount().add(request.getAmount().multiply(ratio)));

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(tokenRepository.findByUser(regularUser)).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(eurCurrencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(eurCurrencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
        when(updater.getRatio(eurCurrencyUnit.getCurrencyName(),currencyUnit.getCurrencyName()))
                .thenReturn(ratio);
        when(updater.createUpdater()).thenReturn(updater);

        mockMvc.perform(post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTransaction.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName")
                        .value(savedTransaction.getCurrencyUnit().getCurrencyName()))
                .andExpect(jsonPath("$.transactionAmount.amount").value(request.getAmount().doubleValue()))
                .andExpect(jsonPath("$.beforeBalance").value(transaction.getBeforeBalance().doubleValue()))
                .andExpect(jsonPath("$.afterBalance").value(transaction.getAfterBalance().doubleValue()))
                .andExpect(jsonPath("$.cardName").value(card.getCardName()))
                .andDo(print());

    }

    @Test
    public void shouldPostSubtractTransactionInOtherCurrency() throws Exception {
        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        CurrencyUnitEntity eurCurrencyUnit = new CurrencyUnitEntity(2L,"EUR",433,1);
        Card card = new Card(1L,currencyUnit,randomValue(), regularUser,"cardName");

        ObjectMapper mapper = new ObjectMapper();
        TransactionRequest request = new TransactionRequest("EUR",randomValue().negate());
        String json = mapper.writeValueAsString(request);

        BigDecimal ratio = BigDecimal.valueOf(1.5);

        TransactionEntity transaction = new TransactionEntity(eurCurrencyUnit,request.getAmount(),false);
        transaction.setUpdater(updater);
        transaction.setCard(card);
        transaction.setBeforeBalance(card.getBalance().getAmount());
        transaction.setAfterBalance(card.getBalance().getAmount().subtract(request.getAmount().multiply(ratio)));

        TransactionEntity savedTransaction = new TransactionEntity(1L,eurCurrencyUnit,request.getAmount(),false);
        savedTransaction.setUpdater(updater);
        savedTransaction.setCard(card);
        savedTransaction.setBeforeBalance(card.getBalance().getAmount());
        savedTransaction.setAfterBalance(card.getBalance().getAmount().subtract(request.getAmount().multiply(ratio)));

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(tokenRepository.findByUser(regularUser)).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(eurCurrencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(eurCurrencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
        when(updater.getRatio(eurCurrencyUnit.getCurrencyName(),currencyUnit.getCurrencyName()))
                .thenReturn(ratio);
        when(updater.createUpdater()).thenReturn(updater);

        mockMvc.perform(post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTransaction.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName")
                        .value(savedTransaction.getCurrencyUnit().getCurrencyName()))
                .andExpect(jsonPath("$.transactionAmount.amount").value(request.getAmount().doubleValue()))
                .andExpect(jsonPath("$.beforeBalance").value(transaction.getBeforeBalance().doubleValue()))
                .andExpect(jsonPath("$.afterBalance").value(transaction.getAfterBalance().doubleValue()))
                .andExpect(jsonPath("$.cardName").value(card.getCardName()))
                .andDo(print());

    }


}