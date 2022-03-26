package wallet.money;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

public interface CurrencyUpdaterProvider {


    BigDecimal getRatio(String currencyFrom,String currencyTo);
    BigDecimal getCurID(String currencyName);
    BigDecimal getCurScale(String currencyName);

    HashMap<String,BigDecimal> getCurrencyHash(String currencyName);

}
