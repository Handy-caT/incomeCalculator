package wallet.money;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CurrencyUpdaterProvider {

    BigDecimal getDecimalPlaces(String currencyString);
    BigDecimal getRatio(String currencyFrom,String currencyTo);
    void addCurrency(String currencyName,BigDecimal decimalPlaces, HashMap<String, BigDecimal> currenciesRatioMap);
    void addRatio(String fromCurrencyName,String toCurrencyName,BigDecimal ratio);
    HashMap<String,BigDecimal> getCurrencyHash(String currencyName);

}
