package com.incomeCalculator.userapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.userapi.repositories.RequestStatisticsRepository;
import com.incomeCalculator.userapi.repositories.UserInfoRepository;
import com.incomeCalculator.userservice.repositories.requests.RequestDestinationRepository;
import com.incomeCalculator.userservice.repositories.requests.RequestRepository;
import com.incomeCalculator.userservice.repositories.requests.RequestSourceRepository;
import com.incomeCalculator.userservice.repositories.requests.ResponseRepository;
import com.incomeCalculator.userservice.repositories.tokens.CookieRepository;
import com.incomeCalculator.userservice.requests.AuthDto;
import com.incomeCalculator.userservice.models.Role;
import com.incomeCalculator.userservice.models.Token;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.user.RoleRepository;
import com.incomeCalculator.userservice.repositories.tokens.TokenRepository;
import com.incomeCalculator.userservice.repositories.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {


    @MockBean
    UserRepository userRepository;
    @MockBean
    CookieRepository cookieRepository;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    RequestRepository requestRepository;
    @MockBean
    ResponseRepository responseRepository;
    @MockBean
    RequestDestinationRepository requestDestinationRepository;
    @MockBean
    RequestSourceRepository requestSourceRepository;
    @MockBean
    RequestStatisticsRepository requestStatisticsRepository;
    @MockBean
    UserInfoRepository userInfoRepository;

    @Autowired
    MockMvc mockMvc;




    public static User getRawUser() {
        return new User(1L,"user","password",new Role("ROLE_USER"));
    }
    public static User getRawAdmin() {
        return new User(1L,"admin","password",new Role("ROLE_ADMIN"));
    }
    public static Token createTokenForUser(User user) {

        String id = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        Date exp = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

        String token;
        token = Jwts.builder()
                .setId(id)
                .setIssuedAt(date)
                .setNotBefore(date)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, user.getLogin())
                .compact();

        return new Token(1L,user,token);
    }

    @Test
    public void registerTest() throws Exception {

        User rawUser = getRawUser();
        AuthDto request = new AuthDto();
        request.setLogin(rawUser.getLogin());
        request.setPassword(rawUser.getPassword());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(request);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(request.getPassword());

        User encodedUser = new User();
        encodedUser.setLogin(rawUser.getLogin());
        encodedUser.setPassword(encodedPassword);
        encodedUser.setRole(rawUser.getRole());

        when(userRepository.save(encodedUser)).thenReturn(encodedUser);
        when(roleRepository.findByRoleName(rawUser.getRole().getRoleName()))
                .thenReturn(Optional.of(rawUser.getRole()));
        when(passwordEncoder.encode(rawUser.getPassword())).thenReturn(encodedPassword);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    JsonNode jsonNode = objectMapper.readTree(json);
                    try {
                        Jwts.parser().setSigningKey(rawUser.getLogin())
                                .parseClaimsJws(jsonNode.get("token").asText());
                    } catch (Exception ex) {
                        fail(ex.getMessage());
                    }
                }).andDo(print());

    }

    @Test
    public void shouldAuthenticateValidUser() throws Exception {

        User rawUser = getRawUser();
        AuthDto request = new AuthDto();
        request.setLogin(rawUser.getLogin());
        request.setPassword(rawUser.getPassword());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(request);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(request.getPassword());

        User encodedUser = new User();
        encodedUser.setLogin(rawUser.getLogin());
        encodedUser.setPassword(encodedPassword);
        encodedUser.setRole(rawUser.getRole());

        when(userRepository.findByLogin(rawUser.getLogin())).thenReturn(Optional.of(encodedUser));
        when(passwordEncoder.matches(rawUser.getPassword(),encodedPassword)).thenReturn(true);

        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    JsonNode jsonNode = objectMapper.readTree(json);
                    try {
                        Jwts.parser().setSigningKey(rawUser.getLogin())
                                .parseClaimsJws(jsonNode.get("token").asText());
                    } catch (Exception ex) {
                        fail(ex.getMessage());
                    }
                }).andDo(print());

    }

    @Test
    public void shouldNotAuthenticateWrongUser() throws Exception {
        User rawUser = getRawUser();
        AuthDto request = new AuthDto();
        request.setLogin(rawUser.getLogin());
        request.setPassword(rawUser.getPassword());


        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(request);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(request.getPassword());

        User encodedUser = new User();
        encodedUser.setLogin(rawUser.getLogin());
        encodedUser.setPassword(encodedPassword);
        encodedUser.setRole(rawUser.getRole());

        when(userRepository.findByLogin(rawUser.getLogin())).thenReturn(Optional.of(encodedUser));
        when(passwordEncoder.matches(rawUser.getPassword()+"1",encodedPassword)).thenReturn(false);

        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

}