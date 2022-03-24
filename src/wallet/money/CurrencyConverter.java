package wallet.money;

import java.math.BigDecimal;
import java.util.*;

public class CurrencyConverter {

    private short mapSize;
    private TreeMap<String,BigDecimal> priorityHash;
    private CurrencyUpdaterProvider currencyUpdater;

    private HashMap<String,HashMap<String, BigDecimal>> converterMapSell;

    CurrencyConverter(CurrencyUpdaterProvider currencyUpdater, short mapSize,List<String> currencyNamesList) {
        this.currencyUpdater = currencyUpdater;
        this.mapSize = mapSize;

        buildHashes(currencyNamesList);
    }
    CurrencyConverter(CurrencyUpdaterProvider currencyUpdater) {
        this.currencyUpdater = currencyUpdater;
        mapSize = 3;
        List<String> currencyNameList = new LinkedList<>();
        currencyNameList.add("USD");
        currencyNameList.add("EUR");
        currencyNameList.add("BYN");

        buildHashes(currencyNameList);
    }
    public  BigDecimal getConvertSellRatio(CurrencyUnit fromCurrency,CurrencyUnit toCurrency) {

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
    public void setConvertSellRatio(CurrencyUnit fromCurrency,CurrencyUnit toCurrency,BigDecimal ratio) {
        converterMapSell.get(fromCurrency.toString()).remove(toCurrency.toString());
        converterMapSell.get(fromCurrency.toString()).put(toCurrency.toString(),ratio);
    }
    public Money convert(Money money, CurrencyUnit currencyToConvertTo) {
        BigDecimal newAmount = money.getAmount().multiply(getConvertSellRatio(money.getCurrency(),currencyToConvertTo));
        return new Money(currencyToConvertTo,newAmount);
    }
    public void setMapSize(short size) {
        mapSize = size;
    }

    private void addCurrency(String currencyName, HashMap<String, BigDecimal> currenciesRatioMap) {
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

    private void buildHashes(List<String> currencyNamesList) {
        int i = 0;

        converterMapSell = new HashMap<>();

        while(i < mapSize) {
            String currencyName = currencyNamesList.get(i);
            HashMap<String,BigDecimal> tempHashMap = currencyUpdater.getCurrencyHash(currencyName);

            addCurrency(currencyName,tempHashMap);
            i++;
        }

    }
}
