package com.incomeCalculator.userapi.repositories;

import com.incomeCalculator.userapi.models.RequestStatistics;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestStatisticsRepository extends JpaRepository<RequestStatistics, Long> {

    RequestStatistics findFirstByOrderByCreateDateTimeDesc();

}