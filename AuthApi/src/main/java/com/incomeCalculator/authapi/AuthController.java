package com.incomeCalculator.authapi;


import com.incomeCalculator.authapi.models.User;
import com.incomeCalculator.authapi.requests.AuthResponse;
import com.incomeCalculator.authapi.requests.UserAuthRequest;
import com.incomeCalculator.authapi.services.JwtTokenService;
import com.incomeCalculator.authapi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@RestController
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

        user = service.saveUser(user);

        String token = tokenService.generateToken(user.getLogin());
        tokenService.saveToken(token,user);
        log.info("User saved, id=" + user.getId() + ", login=" + user.getLogin());

        return new AuthResponse(token);
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
