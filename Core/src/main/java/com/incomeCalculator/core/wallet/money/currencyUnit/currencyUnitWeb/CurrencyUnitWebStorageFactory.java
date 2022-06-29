package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb;

import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorageFactory;

public class CurrencyUnitWebStorageFactory implements CurrencyUnitStorageFactory {

    @Override
    public CurrencyUnitStorage createStorage() {
        return new CurrencyUnitWebStorage();
    }
}
