package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.models.Token;
import com.incomeCalculator.webService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByUser(User user);

}
