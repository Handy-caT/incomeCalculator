package com.incomeCalculator.webService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomeCalculator.webService.modelAssembelrs.RatioModelAssembler;
import com.incomeCalculator.webService.models.Role;
import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.repositories.RoleRepository;
import com.incomeCalculator.webService.repositories.TokenRepository;
import com.incomeCalculator.webService.repositories.UserRepository;
import com.incomeCalculator.webService.requests.AuthResponse;
import com.incomeCalculator.webService.requests.UserAuthRequest;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.CustomUserDetails;
import com.incomeCalculator.webService.services.CustomUserDetailsService;
import com.incomeCalculator.webService.services.UserService;
import io.jsonwebtoken.Jwts;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

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
    CustomUserDetailsService userDetailsService;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    RoleRepository roleRepository;

    @Autowired
    MockMvc mockMvc;

    @TestConfiguration
    static class AdditionalConfig {

        @Bean
        public JwtTokenService getJwtTokenService() {
            return new JwtTokenService();
        }

        @Bean
        public UserService getUserService() {
            return new UserService();
        }

    }


    private static User getRawUser() {
        return new User(1L,"user","password",new Role("ROLE_USER"));
    }
    private static User getRawAdmin() {
        return new User(1L,"admin","password",new Role("ROLE_ADMIN"));
    }

    @Test
    public void registerTest() throws Exception {

        User rawUser = getRawUser();
        UserAuthRequest request = new UserAuthRequest();
        request.setLogin(rawUser.getLogin());
        request.setPassword(rawUser.getPassword());

        User requestedUser = new User();
        requestedUser.setPassword(rawUser.getPassword());
        requestedUser.setLogin(rawUser.getLogin());

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
        UserAuthRequest request = new UserAuthRequest();
        request.setLogin(rawUser.getLogin());
        request.setPassword(rawUser.getPassword());

        User requestedUser = new User();
        requestedUser.setPassword(rawUser.getPassword());
        requestedUser.setLogin(rawUser.getLogin());

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
        UserAuthRequest request = new UserAuthRequest();
        request.setLogin(rawUser.getLogin());
        request.setPassword(rawUser.getPassword());

        User requestedUser = new User();
        requestedUser.setPassword(rawUser.getPassword()+"1");
        requestedUser.setLogin(rawUser.getLogin());

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
                .andExpect(status().isForbidden())
                .andDo(print());

    }

}