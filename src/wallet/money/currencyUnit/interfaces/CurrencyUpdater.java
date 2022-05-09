package wallet.money.currencyUnit.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CurrencyUpdater {


    BigDecimal getRatio(String currencyFrom,String currencyTo);
    long getCurScale(String currencyName);
    BigDecimal getRatioOnDate(String currencyFrom,String currencyTo, Date date);

    Map<String,BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo);


}
