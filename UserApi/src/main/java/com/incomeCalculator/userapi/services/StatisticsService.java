package com.incomeCalculator.userapi.services;

import com.incomeCalculator.userapi.models.RequestStatistics;
import com.incomeCalculator.userapi.repositories.RequestStatisticsRepository;
import com.incomeCalculator.userservice.models.Request;
import com.incomeCalculator.userservice.repositories.RequestDestinationRepository;
import com.incomeCalculator.userservice.repositories.RequestRepository;
import com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    private List<String> apiNames;

    @Autowired
    private RequestRepository repository;
    @Autowired
    private RequestDestinationRepository requestDestinationRepository;
    @Autowired
    private RequestStatisticsRepository requestStatisticsRepository;

    public StatisticsService() {
        apiNames = new ArrayList<>();

        apiNames.add("UserAPI");
        apiNames.add("CardAPI");
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void countRequests() {
        RequestStatistics lastStatistics = requestStatisticsRepository.findFirstByOrderByCreateDateTimeDesc();
        LocalDateTime lastStatisticsDateTime = lastStatistics.getCreateDateTime();

        RequestStatistics newStatistics = new RequestStatistics(repository.countAllByCreateDateTimeAfter(lastStatisticsDateTime),
                repository.findFirstByOrderByCreateDateTimeDesc());

        newStatistics.setCardCount(requestDestinationRepository.countAllByApiNameAndRequest_CreateDateTimeAfter("CardAPI",lastStatisticsDateTime));
        newStatistics.setUserCount(requestDestinationRepository.countAllByApiNameAndRequest_CreateDateTimeAfter("UserAPI",lastStatisticsDateTime));

        log.info("Request statistics counted: " + requestStatisticsRepository.save(newStatistics));
    }
    

}
