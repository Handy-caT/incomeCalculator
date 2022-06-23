package com.incomeCalculator.cardservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.cardservice.controllers.CardController;
import com.incomeCalculator.cardservice.exceptions.CardNotFoundException;
import com.incomeCalculator.cardservice.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.cardservice.modelAssemblers.CardModelAssembler;
import com.incomeCalculator.cardservice.modelAssemblers.TransactionModelAssembler;
import com.incomeCalculator.cardservice.models.Card;
import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import com.incomeCalculator.cardservice.models.TransactionEntity;
import com.incomeCalculator.cardservice.repositories.CardRepository;
import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.cardservice.repositories.RatioRepository;
import com.incomeCalculator.cardservice.repositories.TransactionRepository;
import com.incomeCalculator.cardservice.requests.CardRequest;
import com.incomeCalculator.cardservice.requests.TransactionRequest;
import com.incomeCalculator.cardservice.services.CardService;
import com.incomeCalculator.cardservice.util.CurrencyUpdaterSQL;
import com.incomeCalculator.userservice.models.Role;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.RoleRepository;
import com.incomeCalculator.userservice.repositories.TokenRepository;
import com.incomeCalculator.userservice.repositories.UserRepository;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import static com.incomeCalculator.cardservice.controllers.ModelMocks.getAdminUser;
import static com.incomeCalculator.cardservice.controllers.ModelMocks.getRegularUser;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CardController.class)
@ComponentScan(basePackages = {"com.incomeCalculator.cardservice"})
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
    CurrencyUnitRepository currencyUnitRepository;
    @MockBean
    RatioRepository ratioRepository;
    @MockBean
    CurrencyUpdaterSQL updater;

    private Random random = new SecureRandom();

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

        return value;
    }


    @Autowired
    MockMvc mockMvc;



    @Test
    public void createCardTest() throws Exception {
        User regularUser = new User(1L,"user","password",new Role("ROLE_USER"));

        ObjectMapper objectMapper = new ObjectMapper();
        CardRequest cardRequest = new CardRequest("newCard","USD");
        String json = objectMapper.writeValueAsString(cardRequest);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(null,currencyUnit, BigDecimal.ZERO,regularUser,cardRequest.getCardName());
        Card savedCard = new Card(1L,currencyUnit, BigDecimal.ZERO,regularUser,cardRequest.getCardName());

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(repository.save(card)).thenReturn(savedCard);

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
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

        User regularUser = getRegularUser();

        ObjectMapper objectMapper = new ObjectMapper();
        CardRequest cardRequest = new CardRequest("newCard","USS");
        String json = objectMapper.writeValueAsString(cardRequest);

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(cardRequest.getCurrencyName()))
                .thenThrow(new CurrencyUnitNotFoundException(cardRequest.getCurrencyName()));

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

   @Test
    public void validUserCanGetHisCard() throws Exception {

       User regularUser = getRegularUser();

       CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
       Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), regularUser,"cardName");

       when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
       when(repository.findById(card.getId())).thenReturn(Optional.of(card));
       when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));

       mockMvc.perform(MockMvcRequestBuilders.get("/cards/{id}",card.getId())
                .header("id",regularUser.getId())
                .header("role",regularUser.getRole().getRoleName()))
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

        User regularUser = getRegularUser();

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), regularUser,"cardName");

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(repository.findById(card.getId())).thenThrow(new CardNotFoundException(card.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/{id}",card.getId())
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowInvalidUserGetCard() throws Exception {

        User regularUser = getRegularUser();
        User cardUser = getRegularUser();
        cardUser.setLogin("cardUser");
        cardUser.setId(2L);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), cardUser,"cardName");

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(cardUser.getLogin())).thenReturn(Optional.of(cardUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/{id}",card.getId())
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
                .andDo(print());

    }

    @Test
    public void shouldAllowAdminGetEveryCard() throws Exception {

        User admin = getAdminUser();

        User cardUser = getRegularUser();

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit, BigDecimal.valueOf(10), cardUser,"cardName");

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(cardUser.getLogin())).thenReturn(Optional.of(cardUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/{id}",card.getId())
                        .header("id",admin.getId())
                        .header("role",admin.getRole().getRoleName()))
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

        User regularUser = getRegularUser();

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

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
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

        User regularUser = getRegularUser();

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

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
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
        User regularUser = getRegularUser();

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

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(eurCurrencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(eurCurrencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
        when(updater.getRatio(eurCurrencyUnit.getCurrencyName(),currencyUnit.getCurrencyName()))
                .thenReturn(ratio);
        when(updater.createUpdater()).thenReturn(updater);

        mockMvc.perform(MockMvcRequestBuilders.post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
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
        User regularUser = getRegularUser();

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

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));
        when(currencyUnitRepository.findByCurrencyName(eurCurrencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(eurCurrencyUnit));
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
        when(updater.getRatio(eurCurrencyUnit.getCurrencyName(),currencyUnit.getCurrencyName()))
                .thenReturn(ratio);
        when(updater.createUpdater()).thenReturn(updater);

        mockMvc.perform(MockMvcRequestBuilders.post("/cards/{id}/transactions",card.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
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
    public void shouldAllowUserToDeleteHisCard() throws Exception {
        User regularUser = getRegularUser();

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit,randomValue(), regularUser,"cardName");

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(MockMvcRequestBuilders.delete("/cards/{id}",card.getId())
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole().getRoleName()))
                .andDo(print());

        verify(repository, times(1)).delete(card);

    }

    @Test
    public void shouldAllowAdminToDeleteAnyCard() throws Exception {
        User admin = getAdminUser();
        User regularUser = getRegularUser();

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Card card = new Card(1L,currencyUnit,randomValue(), regularUser,"cardName");

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(card.getId())).thenReturn(Optional.of(card));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(MockMvcRequestBuilders.delete("/cards/{id}",card.getId())
                        .header("id",admin.getId())
                        .header("role",admin.getRole().getRoleName()))
                .andDo(print());

        verify(repository, times(1)).delete(card);

    }

}