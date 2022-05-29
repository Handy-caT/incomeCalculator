package com.incomeCalculator.webService.controllers;


import com.incomeCalculator.webService.models.User;
import com.incomeCalculator.webService.requests.AuthResponse;
import com.incomeCalculator.webService.requests.UserAuthRequest;
import com.incomeCalculator.webService.security.JwtTokenService;
import com.incomeCalculator.webService.services.UserService;
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
        log.info("Registration: " + registrationRequest);
        user.setPassword(registrationRequest.getPassword());
        user.setLogin(registrationRequest.getLogin());

        user = service.saveUser(user);
        log.info("User saved:" + user.toString());

        String token = tokenService.generateToken(user.getLogin());
        tokenService.saveToken(token,user);


        return new AuthResponse(token);
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody UserAuthRequest request) {
        User userEntity = service.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = tokenService.generateToken(userEntity.getLogin());
        tokenService.saveToken(token,userEntity);

        return new AuthResponse(token);
    }

}
