package com.incomeCalculator.cardservice;

import com.incomeCalculator.userservice.models.User;
import com.incomeCalculator.userservice.repositories.user.UserRepository;
import com.incomeCalculator.userservice.services.RequestHandler;
import com.incomeCalculator.userservice.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Import({UserService.class, RequestHandler.class})
@ComponentScan(basePackages = "com.incomeCalculator.cardservice")
@ComponentScan(basePackages = {"com.incomeCalculator.userservice.exceptions"})
@EnableJpaRepositories(basePackages = "com.incomeCalculator.cardservice.repositories",
        basePackageClasses = {com.incomeCalculator.userservice.repositories.user.UserRepository.class})
@EntityScan(basePackages = "com.incomeCalculator.cardservice.models",
        basePackageClasses = {com.incomeCalculator.userservice.models.User.class})
public class CardServiceApplication {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(CardServiceApplication.class, args);
    }

}
