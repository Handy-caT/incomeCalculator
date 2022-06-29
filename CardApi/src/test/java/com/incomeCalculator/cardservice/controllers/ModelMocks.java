package com.incomeCalculator.cardservice.controllers;

import com.incomeCalculator.userservice.models.Role;
import com.incomeCalculator.userservice.models.User;

public class ModelMocks {

    public static User getRegularUser() {
        return new User(1L,"user","password",new Role("ROLE_USER"));
    }
    public static User getAdminUser() {
        return new User(1L,"admin","password",new Role("ROLE_ADMIN"));
    }

}
