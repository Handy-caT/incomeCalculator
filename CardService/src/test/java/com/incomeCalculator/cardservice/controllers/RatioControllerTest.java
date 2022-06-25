package com.incomeCalculator.cardservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.cardservice.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import com.incomeCalculator.cardservice.models.Ratio;
import com.incomeCalculator.cardservice.repositories.CardRepository;
import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.cardservice.repositories.RatioRepository;
import com.incomeCalculator.cardservice.repositories.TransactionRepository;
import com.incomeCalculator.cardservice.requests.RatioRequest;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.RoleRepository;
import com.incomeCalculator.userservice.repositories.TokenRepository;
import com.incomeCalculator.userservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
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
import static com.incomeCalculator.cardservice.controllers.ModelMocks.getAdminUser;
import static com.incomeCalculator.cardservice.controllers.ModelMocks.getRegularUser;

@WebMvcTest(controllers = RatioController.class)
class RatioControllerTest {

    static SecureRandom random = new SecureRandom();

    @MockBean
    RatioRepository repository;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    CurrencyUnitRepository currencyUnitRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    CardRepository cardRepository;
    @MockBean
    TransactionRepository transactionRepository;


    private final String hostName = "http://localhost";

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
        List<Ratio> list = new LinkedList<>();
        list.add(ratio);

        when(repository.findById(id)).thenReturn(Optional.of(ratio));
        when(repository.findAllByDateString(dateString)).thenReturn(list);

        mockMvc.perform(get("/ratios/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.ratio")
                        .value(ratioAmount.doubleValue()))
                .andExpect(jsonPath("$.dateString").value(dateString))
                .andExpect(jsonPath("$.currencyUnit.id").value(currencyUnit.getId()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyUnit.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyUnit.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(RatioController.class)
                                .one(ratio.getId().toString(),"0")).toString()))
                .andExpect(jsonPath("$._links.currencyUnit.href")
                        .value(hostName +  WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andExpect(jsonPath("$._links.ratios.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(RatioController.class)
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

        mockMvc.perform(get("/ratios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..ratioList.size()").value(ratioList.size()))
                .andExpect(jsonPath("$..ratioList[0].ratio")
                        .value(ratioList.get(0).getRatio().doubleValue()))
                .andExpect(jsonPath("$..ratioList[1].ratio")
                        .value(ratioList.get(1).getRatio().doubleValue()))
                .andExpect(jsonPath("$..ratioList[2].ratio")
                        .value(ratioList.get(2).getRatio().doubleValue()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(RatioController.class)
                                .all(Optional.of(dateString)))))
                .andDo(print());
    }

    @Test
    public void shouldNotAllowPostForRegularUser() throws Exception {

        Date date = new Date();

        User regularUser = getRegularUser();

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue();
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(post("/ratios")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowPutForRegularUser() throws Exception {
        Date date = new Date();

        User regularUser = getRegularUser();

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue();
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(put("/ratios/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowDeleteForRegularUser() throws Exception {

        User regularUser = getRegularUser();

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(delete("/ratios/{id}",1L)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void shouldAllowPostForAdmin() throws Exception {

        Date date = new Date();

        User admin = getAdminUser();

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue().setScale(4, RoundingMode.HALF_DOWN);
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Ratio ratioEntity = new Ratio(1L,currencyUnit,ratio,DateFormatter.sqlFormat(date));

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findByCurrencyUnit_CurrencyNameAndDateString(ratioRequest.getCurrencyName(),
                ratioRequest.getDateString())).thenReturn(Optional.empty());
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(repository.save(ratioEntity)).thenReturn(ratioEntity);

        mockMvc.perform(post("/ratios")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ratioEntity.getId()))
                .andExpect(jsonPath("$.ratio")
                        .value(ratio.doubleValue()))
                .andExpect(jsonPath("$.dateString").value(ratioEntity.getDateString()))
                .andExpect(jsonPath("$.currencyUnit.currencyName").value(currencyUnit.getCurrencyName()))
                .andDo(print());

    }

    @Test
    public void shouldAllowPutForAdmin() throws Exception {
        Date date = new Date();

        User admin = getAdminUser();

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal ratio = randomValue();
        RatioRequest ratioRequest = new RatioRequest(1L,"USD",ratio,DateFormatter.sqlFormat(date));
        String json = objectMapper.writeValueAsString(ratioRequest);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Ratio ratioEntity = new Ratio(1L,currencyUnit,ratio,DateFormatter.sqlFormat(date));

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.save(ratioEntity)).thenReturn(ratioEntity);
        when(currencyUnitRepository.findByCurrencyName(currencyUnit.getCurrencyName()))
                .thenReturn(Optional.of(currencyUnit));
        when(repository.findById(ratioEntity.getId())).thenReturn(Optional.of(ratioEntity));


        mockMvc.perform(put("/ratios/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
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

        User admin = getAdminUser();

        BigDecimal ratio = randomValue();

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        Ratio ratioEntity = new Ratio(1L,currencyUnit,ratio,DateFormatter.sqlFormat(date));

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(ratioEntity.getId())).thenReturn(Optional.of(ratioEntity));

        mockMvc.perform(delete("/ratios/{id}",1L)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository,times(1)).delete(ratioEntity);
    }

    @Test
    public void shouldReturnAllOnDate() throws Exception {
        Date date = DateFormatter.sqlParse("12_05_2022");
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
                        .value(ratioList.get(0).getRatio().doubleValue()))
                .andExpect(jsonPath("$..ratioList[1].ratio")
                        .value(ratioList.get(1).getRatio().doubleValue()))
                .andExpect(jsonPath("$..ratioList[2].ratio")
                        .value(ratioList.get(2).getRatio().doubleValue()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(RatioController.class)
                                .all(Optional.of(dateString)))))
                .andDo(print());
    }

}