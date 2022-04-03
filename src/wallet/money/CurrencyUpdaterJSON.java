package wallet.money;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider {

    private String jsonPathString;

    CurrencyUpdaterJSON() {
        
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        return null;
    }

    @Override
    public BigDecimal getCurScale(String currencyName) {
        return null;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        return null;
    }

    @Override
    public BigDecimal getCurID(String currencyName) {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo) {
        return null;
    }
}
