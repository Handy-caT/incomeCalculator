package wallet.money.currencyUpdater;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CurrencyUpdaterProvider {


    BigDecimal getRatio(String currencyFrom,String currencyTo);
    BigDecimal getCurID(String currencyName);
    BigDecimal getCurScale(String currencyName);
    BigDecimal getRatioOnDate(String currencyFrom,String currencyTo, Date date);

    Map<String,BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo);


}
