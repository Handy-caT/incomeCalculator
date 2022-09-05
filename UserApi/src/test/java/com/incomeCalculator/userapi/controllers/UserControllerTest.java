package com.incomeCalculator.userapi.controllers;

import com.incomeCalculator.userapi.repositories.RequestStatisticsRepository;
import com.incomeCalculator.userapi.repositories.UserInfoRepository;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.requests.RequestDestinationRepository;
import com.incomeCalculator.userservice.repositories.requests.RequestRepository;
import com.incomeCalculator.userservice.repositories.requests.RequestSourceRepository;
import com.incomeCalculator.userservice.repositories.requests.ResponseRepository;
import com.incomeCalculator.userservice.repositories.tokens.CookieRepository;
import com.incomeCalculator.userservice.repositories.user.RoleRepository;
import com.incomeCalculator.userservice.repositories.tokens.TokenRepository;
import com.incomeCalculator.userservice.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.incomeCalculator.userapi.controllers.AuthControllerTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class UserControllerTest {

    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    TokenRepository tokenRepository;
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
    @MockBean
    CookieRepository cookieRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldAllowValidUserGetHisInfo() throws Exception {
        User user = getRawUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}", user.getId())
                .header("id", user.getId())
                .header("role", user.getRole().getRoleName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().intValue()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.roleName").value(user.getRole().getRoleName()))
                .andDo(print());

    }

    @Test
    public void shouldNotAllowNotValidUserGetInfo() throws Exception {
        User user = getRawUser();
        User user2 = getRawUser();
        user2.setId(2L);
        user2.setLogin("user2");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        mockMvc.perform(get("/users/{id}", user.getId())
                .header("id", user2.getId())
                .header("role", user2.getRole().getRoleName()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void shouldAllowAfminGetEveryUser() throws Exception {
        User admin = getRawAdmin();
        User user = getRawUser();
        user.setId(2L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/users/{id}", user.getId())
                .header("id", admin.getId())
                .header("role", admin.getRole().getRoleName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().intValue()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.roleName").value(user.getRole().getRoleName()))
                .andDo(print());
    }



}