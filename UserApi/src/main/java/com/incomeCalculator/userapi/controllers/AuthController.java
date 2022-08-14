package com.incomeCalculator.userapi.controllers;


import com.incomeCalculator.userservice.repositories.UserRepository;
import com.incomeCalculator.userservice.requests.AuthDto;
import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.requests.CookieDto;
import com.incomeCalculator.userservice.requests.TokenDto;
import com.incomeCalculator.userservice.services.CookieService;
import com.incomeCalculator.userservice.services.JwtTokenService;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService service;
    private final JwtTokenService tokenService;
    private final CookieService cookieService;

    @Autowired
    private UserRepository userRepository;

    AuthController(UserService service, JwtTokenService tokenService, CookieService cookieService) {
        this.service = service;
        this.tokenService = tokenService;
        this.cookieService = cookieService;
    }

    @PostMapping("/register")
    public TokenDto registerUser(@RequestBody AuthDto registrationRequest) {
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

            return new TokenDto(user.getId(),token);
        }
    }

    @PostMapping("/auth")
    public TokenDto auth(@RequestBody AuthDto request) {
        User userEntity = service.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = tokenService.generateToken(userEntity.getLogin());
        tokenService.saveToken(token,userEntity);
        log.info("User authenticated, id=" + userEntity.getId() + ", login=" + userEntity.getLogin());

        return new TokenDto(userEntity.getId(),token);
    }

    @PostMapping("cookies/register")
    public CookieDto registerCookie(@RequestBody AuthDto request) {
        User userEntity = service.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String cookie = cookieService.generateCookie(userEntity);
        cookieService.saveCookie(cookie, userEntity);
        log.info("Cookie registered, id=" + userEntity.getId() + ", login=" + userEntity.getLogin());

        return new CookieDto(cookie,userEntity.getId());
    }

    @DeleteMapping("cookies/delete")
    public String deleteCookie(@RequestBody CookieDto request) {
        User userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));
        cookieService.deleteCookie(request.getToken());
        log.info("Cookie deleted, id=" + userEntity.getId() + ", login=" + userEntity.getLogin());

        return "Cookie deleted";
    }

}
