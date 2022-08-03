package com.incomeCalculator.userservice.repositories;

import com.incomeCalculator.userservice.models.Request;
import com.incomeCalculator.userservice.models.RequestDestination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RequestDestinationRepository extends JpaRepository<RequestDestination, Long> {

    Long countByApiNameAndIdAfter(String apiName, Long id);

    Long countAllByApiNameAndRequest_CreateDateTimeAfter(String apiName, LocalDateTime request_createDateTime);

}