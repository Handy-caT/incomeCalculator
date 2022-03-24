package wallet.money;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

public interface CurrencyUpdaterProvider {

    BigDecimal getDecimalPlaces(String currencyString);
    BigDecimal getRatio(String currencyFrom,String currencyTo);
    HashMap<String,BigDecimal> getCurrencyHash(String currencyName);

}
