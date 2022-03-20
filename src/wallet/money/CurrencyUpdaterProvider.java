package wallet.money;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CurrencyUpdaterProvider {

    BigDecimal getDecimalPlaces(String currencyString);
    BigDecimal getRatio(String currencyFrom,String currencyTo);
    void addCurrency(String currencyName,BigDecimal decimalPlaces, HashMap<String, BigDecimal> currenciesRatioMap);
    void addRatio(String currencyFrom,String currencyTo,BigDecimal ratio);
    void deleteRatio(String currencyFrom,String currencyTo);
    void deleteCurrency(String currencyName);
    HashMap<String,BigDecimal> getCurrencyHash(String currencyName);

}
