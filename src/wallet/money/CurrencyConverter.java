package wallet.money;

import wallet.CurrencyUnit;

import java.math.BigDecimal;
import java.util.HashMap;

public class CurrencyConverter {

    private static HashMap<CurrencyUnit,HashMap<CurrencyUnit, BigDecimal>> converterMapSell;
    static {
        HashMap<CurrencyUnit,BigDecimal> USDConverter = new HashMap<>();
        USDConverter.put(CurrencyUnit.of("USD"),BigDecimal.valueOf(1));
        USDConverter.put(CurrencyUnit.of("EUR"),BigDecimal.valueOf(0.8929));
        USDConverter.put(CurrencyUnit.of("BYN"),BigDecimal.valueOf(3.80));
        converterMapSell.put(CurrencyUnit.of("USD"),USDConverter);

        HashMap<CurrencyUnit,BigDecimal> EURConverter = new HashMap<>();
        EURConverter.put(CurrencyUnit.of("EUR"),BigDecimal.valueOf(1));
        EURConverter.put(CurrencyUnit.of("USD"),BigDecimal.valueOf(1.05));
        EURConverter.put(CurrencyUnit.of("BYN"),BigDecimal.valueOf(3.95));
        converterMapSell.put(CurrencyUnit.of("EUR"),EURConverter);

        HashMap<CurrencyUnit,BigDecimal> BYNConverter = new HashMap<>();
        BYNConverter.put(CurrencyUnit.of("EUR"),BigDecimal.valueOf(0.2278));
        BYNConverter.put(CurrencyUnit.of("USD"),BigDecimal.valueOf(0.2506));
        BYNConverter.put(CurrencyUnit.of("BYN"),BigDecimal.valueOf(1));
        converterMapSell.put(CurrencyUnit.of("BYN"),BYNConverter);
    }

    public static BigDecimal getConvertSellRatio(CurrencyUnit fromCurrency,CurrencyUnit toCurrency) {
        return converterMapSell.get(fromCurrency).get(toCurrency);
    }

    public static void setConvertSellRatio(CurrencyUnit fromCurrency,CurrencyUnit toCurrency,BigDecimal ratio) {
        converterMapSell.get(fromCurrency).remove(toCurrency);
        converterMapSell.get(fromCurrency).put(toCurrency,ratio);
    }

    public static Money convert(Money money, CurrencyUnit currencyToConvertTo) {
        BigDecimal newAmount = money.getAmount().multiply(getConvertSellRatio(money.getCurrency(),currencyToConvertTo));
        return new Money(currencyToConvertTo,newAmount);
    }

}
