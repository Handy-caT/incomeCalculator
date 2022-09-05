package com.incomeCalculator.cardservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.cardservice.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.cardservice.models.CurrencyUnitEntity;
import com.incomeCalculator.cardservice.repositories.CardRepository;
import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.cardservice.repositories.RatioRepository;
import com.incomeCalculator.cardservice.repositories.TransactionRepository;
import com.incomeCalculator.cardservice.services.CurrencyUnitService;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.user.RoleRepository;
import com.incomeCalculator.userservice.repositories.tokens.TokenRepository;
import com.incomeCalculator.userservice.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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


@WebMvcTest(controllers = CurrencyUnitController.class)
public class CurrencyUnitControllerTest {

    @MockBean
    CurrencyUnitRepository repository;
    @MockBean
    CurrencyUnitService service;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    CardRepository cardRepository;
    @MockBean
    TransactionRepository transactionRepository;
    @MockBean
    RatioRepository ratioRepository;


    private final String hostName = "http://localhost";
    private String bearer = "Bearer ";

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getTestWithParammode0() throws Exception {
        Long parammode = 0L;

        Long id = 1L;
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,"USD",432,1);
        when(repository.findById(id)).thenReturn(Optional.of(currencyUnit));
        when(service.getCurrencyUnitWithParam(id.toString(), parammode)).thenReturn(currencyUnit);

        mockMvc.perform(get("/currencyUnits/{param}?parammode={mode}",id,parammode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andDo(print());
    }

    @Test
    public void getTestWithParammode1() throws Exception {
        Long parammode = 1L;

        Long id = 1L;
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,"USD",432,1);
        when(repository.findByCurrencyName(currencyUnit.getCurrencyName())).thenReturn(Optional.of(currencyUnit));
        when(service.getCurrencyUnitWithParam(currencyUnit.getCurrencyName(), parammode)).thenReturn(currencyUnit);

        mockMvc.perform(get("/currencyUnits/{param}?parammode={mode}"
                        ,currencyUnit.getCurrencyName(),parammode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class)
                                .one(currencyUnit.getId().toString(),"0")).toString()))
                .andDo(print());
    }

    @Test
    public void getTestWithParammode2() throws Exception {
        Long parammode = 2L;

        Long id = 1L;
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(id,"USD",432,1);
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.of(currencyUnit));
        when(service.getCurrencyUnitWithParam(String.valueOf(currencyUnit.getCurrencyId()), parammode)).thenReturn(currencyUnit);

        mockMvc.perform(get("/currencyUnits/{param}?parammode={mode}"
                        ,currencyUnit.getCurrencyId(),parammode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andExpect(jsonPath("$._links.currencyUnits.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class)
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

        mockMvc.perform(get("/currencyUnits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..currencyUnitEntityList.size()").value(currenciesList.size()))
                .andExpect(jsonPath("$..currencyUnitEntityList[0].currencyName")
                        .value(currenciesList.get(0).getCurrencyName()))
                .andExpect(jsonPath("$..currencyUnitEntityList[1].currencyName")
                        .value(currenciesList.get(1).getCurrencyName()))
                .andExpect(jsonPath("$..currencyUnitEntityList[2].currencyName")
                        .value(currenciesList.get(2).getCurrencyName()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + WebMvcLinkBuilder.linkTo(methodOn(CurrencyUnitController.class).all())))
                .andDo(print());
    }

    @Test
    public void shouldNotAllowDeleteForRegularUser() throws Exception {

        User regularUser = getRegularUser();

        when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(delete("/currencyUnits/{id}",1)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowPostForRegularUser() throws Exception {

        User regularUser = getRegularUser();

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"ABC",444,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

       when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowPutForRegularUser() throws Exception {

        User regularUser = getRegularUser();

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"ABC",444,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

       when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(put("/currencyUnits/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",regularUser.getId())
                        .header("role",regularUser.getRole()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldAllowDeleteForAdmin() throws Exception {

        User admin = getAdminUser();

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));

        mockMvc.perform(delete("/currencyUnits/{id}",1)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository,times(1)).delete(currencyUnit);
    }

    @Test
    public void shouldAllowPutForAdmin() throws Exception {

        User admin = getAdminUser();

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.save(currencyUnit)).thenReturn(currencyUnit);

        mockMvc.perform(put("/currencyUnits/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currencyUnit.getId()))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andDo(print());

    }

    @Test
    public void shouldAllowPostForAdmin() throws Exception {

        User admin = getAdminUser();

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        CurrencyUnitEntity currencyUnitNull = new CurrencyUnitEntity(null,"USD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.empty());
        when(repository.save(currencyUnitNull)).thenReturn(currencyUnit);

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currencyUnit.getId()))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andDo(print());

    }

    @Test
    public void shouldntPostNotValidByName() throws Exception {
        User admin = getAdminUser();

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USDD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.empty());
        when(repository.save(currencyUnit)).thenReturn(currencyUnit);

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    public void shouldntPostNotValidByCurrencyId() throws Exception {
        User admin = getAdminUser();

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.of(currencyUnit));
        when(repository.save(currencyUnit)).thenReturn(currencyUnit);

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("id",admin.getId())
                        .header("role",admin.getRole()))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }
}