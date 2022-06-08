package com.incomeCalculator.webService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.modelAssembelrs.RatioModelAssembler;
import com.incomeCalculator.webService.models.Token;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.*;
import com.incomeCalculator.webService.requests.RatioRequest;
import com.incomeCalculator.webService.security.JwtFilter;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.RatioService;
import com.incomeCalculator.webService.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatioController.class)
@AutoConfigureMockMvc(addFilters = false)
class RatioControllerTest {

    static SecureRandom random = new SecureRandom();

    private String bearer = "Bearer ";

    @MockBean
    RatioRepository repository;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    JwtFilter filter;
    @MockBean
    CurrencyUnitRepository currencyUnitRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleRepository roleRepository;

    private final String hostName = "http://localhost";

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public  RatioModelAssembler getRatioModelAssembler() {
            return new RatioModelAssembler();
        }

        @Bean
        public RatioService getRatioService() {
            return new RatioService();
        }

        @Bean
        public JwtTokenService getJwtTokenService() {
            return new JwtTokenService();
        }

        @Bean
        public UserService getUserService() {
            return new UserService();
        }

    }

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

        when(repository.findById(id)).thenReturn(Optional.of(ratio));

        mockMvc.perform(get("/ratios/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.ratio")
                        .value(ratioAmount.setScale(2, RoundingMode.HALF_DOWN)))
                .andExpect(jsonPath("$.dateString").value(dateString))
                .andExpect(jsonPath("$.currencyUnit.id").value(currencyUnit.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyUnit.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyUnit.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + linkTo(methodOn(RatioController.class)
                                .one(ratio.getId().toString(),"0")).toString()))
                .andExpect(jsonPath("$._links.currencyUnit.href")
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andExpect(jsonPath("$._links.ratios.href")
                        .value(hostName + linkTo(methodOn(RatioController.class)
                                .all(Optional.of(ratio.getDateString()))).toString()))
                .andDo(print());
    }

    @Test
    public void shouldReturn404WhenNotFoundById() throws Exception {
        Long id = 1L;
        when(repository.findById(id)).thenThrow(new CurrencyUnitNotFoundException(id));

        mockMvc.perform(get("/ratios/{id}",id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturn404WhenNotFoundByName() throws Exception {
        Date date = new Date();
        String dateString = DateFormatter.sqlFormat(date);
        String name = "ABC";
        when(repository.findByCurrencyUnit_CurrencyNameAndDateString(name,dateString))
                .thenThrow(new CurrencyUnitNotFoundException(name));

        mockMvc.perform(get("/ratios/{param}?parammode={mode}",name,1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturnAll() throws Exception {
        Date date = new Date();
        String dateString = DateFormatter.sqlFormat(date);

        CurrencyUnitEntity usdUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        CurrencyUnitEntity eurUnit = new CurrencyUnitEntity(2L,"EUR",433,1);
        CurrencyUnitEntity bynUnit = new CurrencyUnitEntity(3L,"BYN",434,1);

        Ratio usdRatio = new Ratio(1L,usdUnit,randomValue(),dateString);
        Ratio eurRatio = new Ratio(1L,eurUnit,randomValue(),dateString);
        Ratio bynRatio = new Ratio(1L,bynUnit,randomValue(),dateString);

        List<Ratio> ratioList = new LinkedList<Ratio>();
        ratioList.add(usdRatio);
        ratioList.add(eurRatio);
        ratioList.add(bynRatio);

        when(repository.findAllByDateString(dateString)).thenReturn(ratioList);

        mockMvc.perform(get("/ratios?ondate={date}",dateString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..ratioList.size()").value(ratioList.size()))
                .andExpect(jsonPath("$..ratioList[0].ratio")
                        .value(ratioList.get(0).getRatio().setScale(2, RoundingMode.HALF_DOWN)))
                .andExpect(jsonPath("$..ratioList[1].ratio")
                        .value(ratioList.get(1).getRatio().setScale(2, RoundingMode.HALF_DOWN)))
                .andExpect(jsonPath("$..ratioList[2].ratio")
                        .value(ratioList.get(2).getRatio().setScale(2, RoundingMode.HALF_DOWN)))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + linkTo(methodOn(RatioController.class)
                                .all(Optional.of(dateString)))))
                .andDo(print());
    }

    @Test
    public void shouldNotAllowPostForRegularUser() throws Exception {

        Date date = new Date();

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue();
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));

        mockMvc.perform(post("/ratios")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowPutForRegularUser() throws Exception {
        Date date = new Date();

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue();
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));

        mockMvc.perform(put("/ratios/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowDeleteForRegularUser() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));

        mockMvc.perform(delete("/ratios/{id}",1L)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void shouldAllowPostForAdmin() throws Exception {

        Date date = new Date();

        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue().setScale(4, RoundingMode.HALF_DOWN);
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Ratio ratioEntity = new Ratio(1L,currencyUnit,ratio,DateFormatter.sqlFormat(date));

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findByCurrencyUnit_CurrencyNameAndDateString(ratioRequest.getCurrencyName(),
                ratioRequest.getDateString())).thenReturn(Optional.empty());
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(repository.save(ratioEntity)).thenReturn(ratioEntity);

        mockMvc.perform(post("/ratios")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ratioEntity.getId()))
                .andExpect(jsonPath("$.ratio")
                        .value(ratio))
                .andExpect(jsonPath("$.dateString").value(ratioEntity.getDateString()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andDo(print());

    }

    @Test
    public void shouldAllowPutForAdmin() throws Exception {
        Date date = new Date();

        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue();
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Ratio ratioEntity = new Ratio(1L,currencyUnit,ratio,DateFormatter.sqlFormat(date));

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.save(ratioEntity)).thenReturn(ratioEntity);
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(repository.findById(ratioEntity.getId())).thenReturn(Optional.of(ratioEntity));


        mockMvc.perform(put("/ratios/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ratioEntity.getId()))
                .andExpect(jsonPath("$.ratio")
                        .value(ratio))
                .andExpect(jsonPath("$.dateString").value(ratioEntity.getDateString()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andDo(print());

    }

    @Test
    public void shouldAllowDeleteForAdmin() throws Exception {

        Date date = new Date();

        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        BigDecimal ratio = randomValue();

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Ratio ratioEntity = new Ratio(1L,currencyUnit,ratio,DateFormatter.sqlFormat(date));

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(ratioEntity.getId())).thenReturn(Optional.of(ratioEntity));

        mockMvc.perform(delete("/ratios/{id}",1L)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository,times(1)).delete(ratioEntity);
    }

}