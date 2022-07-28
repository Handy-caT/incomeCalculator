package com.incomeCalculator.userapi.repositories;

import com.incomeCalculator.userapi.models.RequestStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestStatisticsRepository extends JpaRepository<RequestStatistics, Long> {

}