package wallet.money;

import java.math.BigDecimal;
import java.util.*;

public class CurrencyConverter {

    private static short mapSize;
    private static TreeMap<String,BigDecimal> priorityHash;
    private static CurrencyUpdaterProvider currencyUpdater;

    private static HashMap<String,HashMap<String, BigDecimal>> converterMapSell;
    static {
        mapSize = 4;
        converterMapSell = new HashMap<>();
        priorityHash = new TreeMap<>();
        currencyUpdater = new CurrencyUpdaterJSON();

        HashMap<String,BigDecimal> tempHash= currencyUpdater.getCurrencyHash("USD");
        converterMapSell.put("USD",tempHash);
        priorityHash.put("USD",BigDecimal.ZERO);

        tempHash = currencyUpdater.getCurrencyHash("EUR");
        converterMapSell.put("USD",tempHash);
        priorityHash.put("EUR",BigDecimal.ZERO);

        tempHash = currencyUpdater.getCurrencyHash("BYN");
        converterMapSell.put("BYN",tempHash);
        priorityHash.put("BYN",BigDecimal.ZERO);
    }

    public static BigDecimal getConvertSellRatio(CurrencyUnit fromCurrency,CurrencyUnit toCurrency) {

        if(!converterMapSell.containsKey(fromCurrency.toString())) {
            HashMap<String, BigDecimal> temp = currencyUpdater.getCurrencyHash(fromCurrency.toString());
            addCurrency(fromCurrency.toString(),temp);
        }

        BigDecimal fromPriority = priorityHash.remove(fromCurrency.toString());
        BigDecimal toPriority = priorityHash.remove(toCurrency.toString());

        fromPriority = fromPriority.add(BigDecimal.ONE);
        toPriority = toPriority.add(BigDecimal.ONE);

        priorityHash.put(fromCurrency.toString(),fromPriority);
        priorityHash.put(toCurrency.toString(),toPriority);

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

    public static void setMapSize(short size) {
        mapSize = size;
    }

    protected static void addCurrency(String currencyName, HashMap<String, BigDecimal> currenciesRatioMap) {
        if(!converterMapSell.containsKey(currencyName)) {
            if(converterMapSell.size() >= mapSize) {
                String lowPriorityCurrency = priorityHash.firstKey();
                priorityHash.remove(lowPriorityCurrency);
                converterMapSell.remove(lowPriorityCurrency);
            }
            converterMapSell.put(currencyName,currenciesRatioMap);
            priorityHash.put(currencyName,BigDecimal.ZERO);
        }
    }

}
