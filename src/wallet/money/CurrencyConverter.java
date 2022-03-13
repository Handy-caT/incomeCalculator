package wallet.money;

import wallet.CurrencyUnit;

import java.math.BigDecimal;
import java.util.HashMap;

public class CurrencyConverter {

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

    public static BigDecimal getConvertSellRatio(CurrencyUnit fromCurrency,CurrencyUnit toCurrency) {
        return converterMapSell.get(fromCurrency.toString()).get(toCurrency.toString());
    }

    public static void setConvertSellRatio(CurrencyUnit fromCurrency,CurrencyUnit toCurrency,BigDecimal ratio) {
        converterMapSell.get(fromCurrency.toString()).remove(toCurrency.toString());
        converterMapSell.get(fromCurrency.toString()).put(toCurrency.toString(),ratio);
    }

    public static Money convert(Money money, CurrencyUnit currencyToConvertTo) {
        BigDecimal newAmount = money.getAmount().multiply(getConvertSellRatio(money.getCurrency(),currencyToConvertTo));
        return new Money(currencyToConvertTo,newAmount);
    }

}
