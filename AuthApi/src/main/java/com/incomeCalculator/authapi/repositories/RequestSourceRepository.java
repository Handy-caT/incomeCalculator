package com.incomeCalculator.authapi.repositories;

import com.incomeCalculator.authapi.models.RequestSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestSourceRepository extends JpaRepository<RequestSource, Long> {
    
}