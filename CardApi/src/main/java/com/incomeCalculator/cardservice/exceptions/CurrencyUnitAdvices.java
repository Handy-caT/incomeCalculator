package com.incomeCalculator.cardservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CurrencyUnitAdvices {

    @ResponseBody
    @ExceptionHandler(CurrencyUnitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String currencyUnitNotFoundHandler(CurrencyUnitNotFoundException ex) {
        return ex.getMessage();
    }

}
