package com.incomeCalculator.userapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RequestStatisticsNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(RequestStatisticsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String cardNotFoundException(RequestStatisticsNotFoundException ex) {
        return ex.getMessage();
    }

}
