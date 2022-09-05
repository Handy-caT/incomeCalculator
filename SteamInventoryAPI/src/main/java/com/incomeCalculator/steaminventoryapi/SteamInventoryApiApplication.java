package com.incomeCalculator.steaminventoryapi;

import com.incomeCalculator.userservice.repositories.user.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.incomeCalculator.steaminventoryapi",
        basePackageClasses = {com.incomeCalculator.userservice.services.UserService.class})
@ComponentScan(basePackages = "com.incomeCalculator.userservice.exceptions")
@EnableJpaRepositories(basePackages = "com.incomeCalculator.steaminventoryapi.repositories",
        basePackageClasses = {UserRepository.class})
@EntityScan(basePackages = "com.incomeCalculator.steaminventoryapi.models",
        basePackageClasses = {com.incomeCalculator.userservice.models.User.class})
public class SteamInventoryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteamInventoryApiApplication.class, args);
    }

}
