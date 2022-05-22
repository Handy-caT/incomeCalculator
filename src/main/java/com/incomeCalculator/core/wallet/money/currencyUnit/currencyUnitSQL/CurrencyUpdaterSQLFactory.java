package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL;

import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

public class CurrencyUpdaterSQLFactory implements CurrencyUpdaterFactory {

    public static final String propertyName = "CurrencyUpdaterSQLTableName";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    @Override
    public CurrencyUpdater createUpdater() {
        String tableName = (String) propertiesStorage.getProperty(propertyName);
        try {
            if (tableName == null) {
                return new CurrencyUpdaterSQL();
            } else {
                return new CurrencyUpdaterSQL(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
