package com.incomeCalculator.userservice.repositories;


import com.incomeCalculator.userservice.models.Token;
import com.incomeCalculator.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByUser(User user);
    Optional<Token> findByToken(String token);

}
