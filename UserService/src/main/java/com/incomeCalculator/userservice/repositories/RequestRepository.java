package com.incomeCalculator.userservice.repositories;

import com.incomeCalculator.userservice.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

}