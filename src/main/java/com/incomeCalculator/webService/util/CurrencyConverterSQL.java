package com.incomeCalculator.webService.util;

import com.incomeCalculator.core.wallet.money.CurrencyConverter;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class CurrencyConverterSQL extends CurrencyConverter {

    public CurrencyConverterSQL(CurrencyUpdaterFactory factory) {
        super(factory);
    }
}
