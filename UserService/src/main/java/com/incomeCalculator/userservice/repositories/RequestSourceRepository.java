package com.incomeCalculator.userservice.repositories;

import com.incomeCalculator.userservice.models.RequestSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestSourceRepository extends JpaRepository<RequestSource, Long> {

}