package com.incomeCalculator.webService.util;

import com.incomeCalculator.core.wallet.money.CurrencyConverter;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

public class CurrencyConverterSQL extends CurrencyConverter {

    public CurrencyConverterSQL(CurrencyUpdaterFactory currencyUpdaterFactory) {
        super(currencyUpdaterFactory);
    }
}
