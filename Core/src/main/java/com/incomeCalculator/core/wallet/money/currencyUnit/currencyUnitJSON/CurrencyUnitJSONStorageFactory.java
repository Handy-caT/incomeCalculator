package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON;

import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorageFactory;

public class CurrencyUnitJSONStorageFactory implements CurrencyUnitStorageFactory {

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    public static final String propertyName = "CurrencyUnitStoragePath";

    @Override
    public CurrencyUnitStorage createStorage() {
        String jsonPath = (String) propertiesStorage.getProperty(propertyName);
        try {
            if (jsonPath == null) {
                return new CurrencyUnitJSONStorage();
            } else {
                return new CurrencyUnitJSONStorage(jsonPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
