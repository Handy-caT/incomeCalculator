package com.incomeCalculator.userapi.controllers;

import com.incomeCalculator.userapi.models.UserInfo;
import com.incomeCalculator.userapi.repositories.UserInfoRepository;
import com.incomeCalculator.userapi.requests.UserInfoDto;
import com.incomeCalculator.userapi.services.UserInfoService;
import com.incomeCalculator.userservice.exceptions.PermissionException;
import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.UserRepository;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserInfoController {

    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);

    private final UserInfoRepository repository;
    private final UserRepository userRepository;


    @Autowired
    private RequestHandler handler;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;

    public UserInfoController(UserInfoRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users/checkEmailAvailability")
    public boolean checkEmailAvailability(String email) {
        return !repository.findByEmail(email).isPresent();
    }

    @GetMapping("/users/{id}/info")
    public UserInfo getUserInfo(@PathVariable Long id, HttpServletRequest request) {
        User authUser = handler.getUserFromRequest(request);
        if(authUser.getId().equals(id) || userService.isAdmin(authUser)) {
            return repository.findByUser_Id(id).orElseThrow(() -> new UserNotFoundException(id));
        } else {
            throw new PermissionException();
        }
    }

    @PostMapping("/users/{id}/info")
    public UserInfo updateUserInfo(@PathVariable Long id, @RequestBody UserInfoDto userInfo, HttpServletRequest request) {
        User authUser = handler.getUserFromRequest(request);
        if(authUser.getId().equals(id) || userService.isAdmin(authUser)) {
            return userInfoService.saveUserInfo(userInfo,authUser);
        } else {
            throw new PermissionException();
        }
    }

}
