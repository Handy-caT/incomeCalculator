package com.incomeCalculator.cardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"com.incomeCalculator.userservice.exceptions"})
@EntityScan(basePackages = {"com.incomeCalculator.userservice.models", "com.incomeCalculator.cardservice.models"})
@EnableJpaRepositories(basePackages = {"com.incomeCalculator.userservice.repositories"})
public class CardServiceApplication {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(CardServiceApplication.class, args);
    }

}
