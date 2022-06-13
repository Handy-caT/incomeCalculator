package com.incomeCalculator.core.wallet.money;

import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.CurrencyUnit;
import com.incomeCalculator.core.wallet.money.currencyUnit.StrictCurrencyUnit;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSONFactory;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWebFactory;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

import java.math.BigDecimal;

public class CurrencyConverter {

    private static CurrencyConverter instance;

    public static final String propertyName = "CurrencyUnitStorageType";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    private final CurrencyUpdater currencyUpdater;

    protected CurrencyConverter(CurrencyUpdaterFactory currencyUpdaterFactory) {
        this.currencyUpdater = currencyUpdaterFactory.createUpdater();
    }

    public  BigDecimal getConvertSellRatio(CurrencyUnit currencyFromUnit, CurrencyUnit currencyToUnit) {
        return currencyUpdater.getRatio(currencyFromUnit.getCurrencyName(),currencyToUnit.getCurrencyName());
    }
    public Money convert(Money money, CurrencyUnit currencyToConvertToUnit) {
        BigDecimal newAmount = money.getAmount().multiply(getConvertSellRatio(money.getCurrency(),currencyToConvertToUnit));
        return new Money(currencyToConvertToUnit,newAmount);
    }

    private static CurrencyConverter createInstance() {
        String updaterType = (String) propertiesStorage.getProperty(propertyName);
        switch (updaterType) {
            case "CurrencyUpdaterJSON":
                return new CurrencyConverter(new CurrencyUpdaterJSONFactory());
            case "CurrencyUpdaterSQL":
                return new CurrencyConverter(new CurrencyUpdaterSQLFactory());
            case "OptimizedUpdater":
                return new CurrencyConverter(new OptimizedUpdater());
            default:
                return new CurrencyConverter(new CurrencyUpdaterWebFactory());
        }
    }

    public static CurrencyConverter getInstance() {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
    }
}
