package com.incomeCalculator.authapi.repositories;


import com.incomeCalculator.authapi.models.Token;
import com.incomeCalculator.authapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByUser(User user);
    Optional<Token> findByToken(String token);

}
