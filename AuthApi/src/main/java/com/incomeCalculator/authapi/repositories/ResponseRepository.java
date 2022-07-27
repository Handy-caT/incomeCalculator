package com.incomeCalculator.authapi.repositories;

import com.incomeCalculator.authapi.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {

}