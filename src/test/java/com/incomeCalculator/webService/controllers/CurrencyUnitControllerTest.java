package com.incomeCalculator.webService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.modelAssembelrs.RatioModelAssembler;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.modelAssembelrs.CurrencyUnitModelAssembler;
import com.incomeCalculator.webService.models.Role;
import com.incomeCalculator.webService.models.Token;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RoleRepository;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import com.incomeCalculator.webService.security.JwtFilter;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.CurrencyUnitService;
import com.incomeCalculator.webService.services.CustomUserDetailsService;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CurrencyUnitController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    CustomUserDetailsService customUserDetailsService;
    @MockBean
    RoleRepository roleRepository;

    private final String hostName = "http://localhost";
    private String bearer = "Bearer ";

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public CurrencyUnitModelAssembler getCurrencyUnitModelAssembler() {
            return new CurrencyUnitModelAssembler();
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
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class)
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
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class)
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
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class).all()).toString()))
                .andExpect(jsonPath("$._links.self.href")
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class)
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
                        .value(hostName + linkTo(methodOn(CurrencyUnitController.class).all())))
                .andDo(print());
    }

    @Test
    public void shouldNotAllowDeleteForRegularUser() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(userRepository.findByLogin(regularUser.getLogin())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(delete("/currencyUnits/{id}",1)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowPostForRegularUser() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"ABC",444,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldNotAllowPutForRegularUser() throws Exception {

        User regularUser = AuthControllerTest.getRawUser();
        Token tokenEntity = AuthControllerTest.createTokenForUser(regularUser);

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"ABC",444,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));

        mockMvc.perform(put("/currencyUnits/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void shouldAllowDeleteForAdmin() throws Exception {

        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));

        mockMvc.perform(delete("/currencyUnits/{id}",1)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository,times(1)).delete(currencyUnit);
    }

    @Test
    public void shouldAllowPutForAdmin() throws Exception {

        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.save(currencyUnit)).thenReturn(currencyUnit);

        mockMvc.perform(put("/currencyUnits/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currencyUnit.getId()))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andDo(print());

    }

    @Test
    public void shouldAllowPostForAdmin() throws Exception {

        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.empty());
        when(repository.save(currencyUnit)).thenReturn(currencyUnit);

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currencyUnit.getId()))
                .andExpect(jsonPath("$.currencyName").value(currencyUnit.getCurrencyName()))
                .andExpect(jsonPath("$.currencyId").value(currencyUnit.getCurrencyId()))
                .andExpect(jsonPath("$.currencyScale").value(currencyUnit.getCurrencyScale()))
                .andDo(print());

    }

    @Test
    public void shouldntPostNotValidByName() throws Exception {
        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USDD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.empty());
        when(repository.save(currencyUnit)).thenReturn(currencyUnit);

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    public void shouldntPostNotValidByCurrencyId() throws Exception {
        User admin = AuthControllerTest.getRawAdmin();
        Token tokenEntity = AuthControllerTest.createTokenForUser(admin);

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyUnitEntity currencyUnit = new CurrencyUnitEntity(1L,"USD",432,1);
        String json = objectMapper.writeValueAsString(currencyUnit);

        when(tokenRepository.findByToken(tokenEntity.getToken())).thenReturn(Optional.of(tokenEntity));
        when(repository.findById(currencyUnit.getId())).thenReturn(Optional.of(currencyUnit));
        when(repository.findByCurrencyId(currencyUnit.getCurrencyId())).thenReturn(Optional.of(currencyUnit));
        when(repository.save(currencyUnit)).thenReturn(currencyUnit);

        mockMvc.perform(post("/currencyUnits")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization",bearer + tokenEntity.getToken()))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }
}