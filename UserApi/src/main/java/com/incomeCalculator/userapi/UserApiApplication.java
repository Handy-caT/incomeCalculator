package com.incomeCalculator.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan("com.incomeCalculator")
@EnableJpaRepositories({"com.incomeCalculator.userservice.repositories.user","com.incomeCalculator.userapi.repositories",
    "com.incomeCalculator.userservice.repositories.tokens"})
@EntityScan({"com.incomeCalculator.userservice.models","com.incomeCalculator.userapi.models"})
public class UserApiApplication {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }

}
