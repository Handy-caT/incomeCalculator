package com.incomeCalculator.userapi.exceptions;

public class RequestStatisticsNotFoundException extends RuntimeException{

    public RequestStatisticsNotFoundException(Long id) {
        super("Request statistics with id " + id + " not found");
    }

}
