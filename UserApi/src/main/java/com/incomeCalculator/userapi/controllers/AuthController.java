package com.incomeCalculator.userapi.controllers;


import com.incomeCalculator.userservice.requests.AuthResponse;
import com.incomeCalculator.userservice.requests.UserAuthRequest;
import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.services.JwtTokenService;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService service;
    private final JwtTokenService tokenService;

    AuthController(UserService service, JwtTokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public AuthResponse registerUser(@RequestBody UserAuthRequest registrationRequest) {
        User user = new User();
        user.setPassword(registrationRequest.getPassword());
        user.setLogin(registrationRequest.getLogin());

        try {
            User potentialCopy = service.findByLogin(user.getLogin());

            throw new IllegalArgumentException("User with this login already exists");
        } catch (UserNotFoundException e) {

            user = service.saveUser(user);

            String token = tokenService.generateToken(user.getLogin());
            tokenService.saveToken(token, user);
            log.info("User saved, id=" + user.getId() + ", login=" + user.getLogin());

            return new AuthResponse(token);
        }
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody UserAuthRequest request) {
        User userEntity = service.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = tokenService.generateToken(userEntity.getLogin());
        tokenService.saveToken(token,userEntity);
        log.info("User authenticated, id=" + userEntity.getId() + ", login=" + userEntity.getLogin());

        return new AuthResponse(token);
    }

}
