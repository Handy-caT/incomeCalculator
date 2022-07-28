package com.incomeCalculator.userservice.repositories;

import com.incomeCalculator.userservice.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {

}