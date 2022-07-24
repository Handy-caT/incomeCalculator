package com.incomeCalculator.authapi.repositories;

import com.incomeCalculator.authapi.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

}