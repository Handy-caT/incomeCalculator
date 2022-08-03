package com.incomeCalculator.userservice.repositories;

import com.incomeCalculator.userservice.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Long countAllByCreateDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Long countAllByCreateDateTimeAfter(LocalDateTime startDateTime);

    List<Request> findAllByCreateDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Request findFirstByOrderByCreateDateTimeDesc();

}