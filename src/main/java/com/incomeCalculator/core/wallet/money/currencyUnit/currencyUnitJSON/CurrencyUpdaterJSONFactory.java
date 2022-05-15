package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON;

import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

public class CurrencyUpdaterJSONFactory implements CurrencyUpdaterFactory {

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    @Override
    public CurrencyUpdater createUpdater() {
        String jsonPathString = (String) propertiesStorage.getProperty("CurrencyUpdaterPath");
        try {
            if (jsonPathString == null) {
                return new CurrencyUpdaterJSON();
            } else {
                return new CurrencyUpdaterJSON(jsonPathString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
