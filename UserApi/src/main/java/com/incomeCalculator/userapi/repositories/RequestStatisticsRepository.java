package com.incomeCalculator.userapi.repositories;

import com.incomeCalculator.userapi.models.RequestStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestStatisticsRepository extends JpaRepository<RequestStatistics, Long> {

}