package wallet.money;

import org.json.simple.parser.ParseException;
import wallet.money.currencyUnit.StrictCurrencyUnit;
import wallet.money.currencyUpdater.CurrencyUpdaterJSON;
import wallet.money.currencyUpdater.CurrencyUpdaterProvider;
import wallet.money.currencyUpdater.CurrencyUpdaterWeb;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class CurrencyConverter {

    private static CurrencyConverter instance;

    public static String propertiesString = "properties/config.properties";

    private short mapSize;
    private TreeMap<String,BigDecimal> priorityHash;
    private CurrencyUpdaterProvider currencyUpdater;

    private List<String> currencyNamesList;
    private Map<String,Map<String, BigDecimal>> converterMapSell;

    private CurrencyConverter(CurrencyUpdaterProvider currencyUpdater, short mapSize,List<String> currencyNamesList) {
        this.currencyUpdater = currencyUpdater;
        this.mapSize = mapSize;
        this.currencyNamesList = currencyNamesList;

        buildHashes(currencyNamesList);
    }
    private CurrencyConverter(CurrencyUpdaterProvider currencyUpdater) {
        this.currencyUpdater = currencyUpdater;
        mapSize = 3;
        List<String> currencyNamesList = new LinkedList<>();
        currencyNamesList.add("USD");
        currencyNamesList.add("EUR");
        currencyNamesList.add("BYN");

        this.currencyNamesList = currencyNamesList;

        buildHashes(currencyNamesList);
    }

    public  BigDecimal getConvertSellRatio(StrictCurrencyUnit currencyFromUnit, StrictCurrencyUnit currencyToUnit) {

        if(!currencyNamesList.contains(currencyFromUnit.toString())) {
            addCurrency(currencyFromUnit);
        }
        if(!currencyNamesList.contains(currencyToUnit.toString())) {
            addRatioForAllCurrencies(currencyToUnit);
        }

        BigDecimal fromPriority = priorityHash.remove(currencyFromUnit.toString());

        if(priorityHash.containsKey(currencyToUnit.toString())) {
            BigDecimal toPriority = priorityHash.remove(currencyToUnit.toString());
            toPriority = toPriority.add(BigDecimal.ONE);
            priorityHash.put(currencyToUnit.toString(), toPriority);
        }

        fromPriority = fromPriority.add(BigDecimal.ONE);
        priorityHash.put(currencyFromUnit.toString(), fromPriority);

        return converterMapSell.get(currencyFromUnit.toString()).get(currencyToUnit.toString());
    }
    private void setConvertSellRatio(StrictCurrencyUnit currencyFromUnit, StrictCurrencyUnit currencyToUnit, BigDecimal ratio) {
        converterMapSell.get(currencyFromUnit.toString()).remove(currencyToUnit.toString());
        converterMapSell.get(currencyFromUnit.toString()).put(currencyToUnit.toString(),ratio);
    }
    public Money convert(Money money, StrictCurrencyUnit currencyToConvertToUnit) {
        BigDecimal newAmount = money.getAmount().multiply(getConvertSellRatio(currencyToConvertToUnit,money.getCurrency()));
        return new Money(currencyToConvertToUnit,newAmount);
    }

    public void setMapSize(short size) {
        mapSize = size;
    }

    private void addCurrency(StrictCurrencyUnit currencyUnit) {
        if(!currencyNamesList.contains(currencyUnit.toString())) {
            if (currencyNamesList.size() >= mapSize) {
                String currencyToRemove = priorityHash.firstKey();

                priorityHash.remove(currencyToRemove);
                currencyNamesList.remove(currencyToRemove);
                converterMapSell.remove(currencyToRemove);
            }
            currencyNamesList.add(currencyUnit.toString());
            priorityHash.put(currencyUnit.toString(), BigDecimal.ZERO);
            Map<String, BigDecimal> temp = currencyUpdater.getCurrencyRatiosMap(currencyUnit.toString(), currencyNamesList);
            converterMapSell.put(currencyUnit.toString(), temp);
        }
    }
    private void addRatioForAllCurrencies(StrictCurrencyUnit currencyToUnit) {
        for(String currencyFrom : currencyNamesList) {
            BigDecimal ratio = currencyUpdater.getRatio(currencyFrom,currencyToUnit.toString());
            converterMapSell.get(currencyFrom).put(currencyToUnit.toString(),ratio);
        }
    }

    private void buildHashes(List<String> currencyNamesList) {
        priorityHash = new TreeMap<>();

        converterMapSell = new HashMap<>();
        for (String currencyTo : currencyNamesList) {
            Map<String,BigDecimal> tempMap = currencyUpdater.getCurrencyRatiosMap(currencyTo,currencyNamesList);
            converterMapSell.put(currencyTo,tempMap);
            priorityHash.put(currencyTo,BigDecimal.ZERO);
        }

    }

    private static CurrencyConverter createInstance() throws IOException, ParseException {
        FileInputStream fis = new FileInputStream(propertiesString);
        Properties properties = new Properties();
        properties.load(fis);

        String jsonPathString = (String) properties.get("CurrencyUnitStorageType");
        if(jsonPathString == null) {
            return new CurrencyConverter(CurrencyUpdaterWeb.getInstance());
        } else {
            return new CurrencyConverter(CurrencyUpdaterJSON.getInstance());
        }
    }

    public static CurrencyConverter getInstance() throws IOException, ParseException {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
    }
}
