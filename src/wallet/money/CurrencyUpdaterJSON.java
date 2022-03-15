package wallet.money;

import java.math.BigDecimal;
import java.util.HashMap;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider{

    private String JSONFileName = "currencies.json";

    private static HashMap<String,HashMap<String, BigDecimal>> converterMapSell;
    static {
        converterMapSell = new HashMap<>();



        HashMap<String,BigDecimal> USDConverter = new HashMap<>();
        USDConverter.put("USD",BigDecimal.valueOf(1));
        USDConverter.put("EUR",BigDecimal.valueOf(0.8929));
        USDConverter.put("BYN",BigDecimal.valueOf(3.80));
        converterMapSell.put("USD",USDConverter);

        HashMap<String,BigDecimal> EURConverter = new HashMap<>();
        EURConverter.put("EUR",BigDecimal.valueOf(1));
        EURConverter.put("USD",BigDecimal.valueOf(1.05));
        EURConverter.put("BYN",BigDecimal.valueOf(3.95));
        converterMapSell.put("EUR",EURConverter);

        HashMap<String,BigDecimal> BYNConverter = new HashMap<>();
        BYNConverter.put("EUR",BigDecimal.valueOf(0.2278));
        BYNConverter.put("USD",BigDecimal.valueOf(0.2506));
        BYNConverter.put("BYN",BigDecimal.valueOf(1));
        converterMapSell.put("BYN",BYNConverter);
    }

    @Override
    public short getDecimalPlaces(String currencyString) {
        return 0;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        return null;
    }

    @Override
    public void addCurrency(String currencyName, HashMap<String, BigDecimal> currenciesRatioMap) {

    }

    @Override
    public void saveCurrenciesState() {

    }
}
