package wallet.money;

import org.json.simple.parser.ParseException;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.StrictCurrencyUnit;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSON;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSONFactory;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWebFactory;
import wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWeb;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
        if(updaterType.equals("CurrencyUpdaterJSON")) {
            return new CurrencyConverter(new CurrencyUpdaterJSONFactory());
        } else if(updaterType.equals("CurrencyUpdaterSQL")) {
            return new CurrencyConverter(new CurrencyUpdaterSQLFactory());
        }else if(updaterType.equals("OptimizedUpdater")) {
            return new CurrencyConverter(new OptimizedUpdater());
        }else {
            return new CurrencyConverter(new CurrencyUpdaterWebFactory());
        }
    }

    public static CurrencyConverter getInstance() throws IOException, ParseException {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
    }
}
