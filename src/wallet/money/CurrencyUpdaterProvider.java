package wallet.money;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CurrencyUpdaterProvider {

    short getDecimalPlaces(String currencyString);
    BigDecimal getRatio(String currencyFrom,String currencyTo);
    void addCurrency(String currencyName, HashMap<String, BigDecimal> currenciesRatioMap);
    HashMap<String,BigDecimal> getCurrencyHash(String currencyName);
    void saveCurrenciesState();

}
