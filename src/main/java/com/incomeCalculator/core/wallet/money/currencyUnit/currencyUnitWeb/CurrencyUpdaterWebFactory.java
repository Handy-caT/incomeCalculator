package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb;

import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

public class CurrencyUpdaterWebFactory implements CurrencyUpdaterFactory {

    @Override
    public CurrencyUpdater createUpdater() {
        return new CurrencyUpdaterWeb();
    }
}
