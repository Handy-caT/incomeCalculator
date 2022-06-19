package com.incomeCalculator.userservice;

import com.incomeCalculator.userservice.exceptions.UserNotFoundException;
import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestHandler {

    @Autowired
    UserRepository repository;

    public User getUserFromRequest(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getHeader("id"));
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user;
    }

}
