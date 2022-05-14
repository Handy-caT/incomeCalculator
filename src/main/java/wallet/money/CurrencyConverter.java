package wallet.money;

import wallet.PropertiesStorage;
import wallet.money.currencyUnit.StrictCurrencyUnit;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSONFactory;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWebFactory;
import wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

import java.math.BigDecimal;

public class CurrencyConverter {

    private static CurrencyConverter instance;

    public static final String propertyName = "CurrencyUnitStorageType";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    private final CurrencyUpdater currencyUpdater;

    private CurrencyConverter(CurrencyUpdaterFactory currencyUpdaterFactory) {
        this.currencyUpdater = currencyUpdaterFactory.createUpdater();
    }

    public  BigDecimal getConvertSellRatio(StrictCurrencyUnit currencyFromUnit, StrictCurrencyUnit currencyToUnit) {
        return currencyUpdater.getRatio(currencyFromUnit.toString(),currencyToUnit.toString());
    }
    public Money convert(Money money, StrictCurrencyUnit currencyToConvertToUnit) {
        BigDecimal newAmount = money.getAmount().multiply(getConvertSellRatio(currencyToConvertToUnit,money.getCurrency()));
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
