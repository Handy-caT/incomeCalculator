package com.incomeCalculator.userservice.repositories;

import com.incomeCalculator.userservice.models.Request;
import com.incomeCalculator.userservice.models.RequestDestination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestDestinationRepository extends JpaRepository<RequestDestination, Long> {

    Long countByApiNameAndIdAfter(String apiName, Long id);

}