package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON;

import com.incomeCalculator.core.wallet.PropertiesStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.core.wallet.money.util.LocalJSONConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWebFactory;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWeb;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CurrencyUpdaterJSON implements CurrencyUpdater {

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private static String jsonPathString;
    private static JSONArray currencyJSONArray;
    private static String dir = "json/";
    private static final String jsonEnding = ".json";

    private static final String BYNString = "BYN";

    public static final String defaultFileName = "currencyUpdater";
    public static final String propertyName = "CurrencyUpdaterPath";

    private static String dateString;

    protected CurrencyUpdaterJSON() throws IOException {
        CurrencyUpdaterJSONBuilder builder = CurrencyUpdaterJSONBuilder.getInstance();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        currencyJSONArray = builder.getResult();

        Date date = new Date();
        dateString = DateFormatter.sqlFormat(date);
        if(jsonPathString == null) {
            jsonPathString = dir+defaultFileName + dateString + jsonEnding;
        } else {
            File file = new File(jsonPathString);
            jsonPathString = changeDateInString(jsonPathString);
            File newFile = new File(jsonPathString);
            file.renameTo(newFile);
        }
        propertiesStorage.addProperty(propertyName,jsonPathString);

        File file = new File(jsonPathString);

        FileWriter fileWriter = new FileWriter(file);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    protected CurrencyUpdaterJSON(List<String> buildingPlan) throws IOException {
        Date date = new Date();
        dateString = DateFormatter.sqlFormat(date);

        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = dir+defaultFileName +dateString + jsonEnding;
        propertiesStorage.addProperty(propertyName,jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    protected CurrencyUpdaterJSON(String jsonPathString) throws IOException, ParseException {
        CurrencyUpdaterJSON.jsonPathString = jsonPathString;
        Date date = new Date();
        dateString = DateFormatter.sqlFormat(date);
        if(Objects.equals(dateString,getDateFromName(jsonPathString))) {

            JSONParser jsonParser = new JSONParser();

            FileReader fileReader = new FileReader(jsonPathString);
            currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } else {
           new CurrencyUpdaterJSON();
        }
    }

    public void update() throws IOException {
        new CurrencyUpdaterJSON();
    }

    public String getDate() {
        return dateString;
    }

    private String getDateFromName(String name) {
        String dateString = name.substring(name.length() - 15);
        dateString = dateString.substring(0,10);
        return dateString;
    }
    private String changeDateInString(String name) {
        String start = name.substring(0,name.length()-15);
        Date date = new Date();
        return start + DateFormatter.sqlFormat(date) + ".json";
    }

    private JSONObject getJSONObjectByCurrencyString(String currencyName) {
        JSONObject currencyJSONObject;
        JSONObject result = null;

        for( Object currencyObject : currencyJSONArray) {
            currencyJSONObject = (JSONObject) currencyObject;
            String tempCurrencyName = (String) currencyJSONObject.get("currencyName");
            if(Objects.equals(tempCurrencyName, currencyName)) {
                result = currencyJSONObject;
                break;
            }
        }
        return result;
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        CurrencyUpdaterWebFactory factory = new CurrencyUpdaterWebFactory();
        CurrencyUpdaterWeb currencyUpdaterWeb = (CurrencyUpdaterWeb) factory.createUpdater();
        return currencyUpdaterWeb.getRatioOnDate(currencyFrom,currencyTo,date);
    }

    @Override
    public long getCurScale(String currencyName) {
        JSONObject currencyObject = getJSONObjectByCurrencyString(currencyName);
        return (long)currencyObject.get("currencyScale");
    }

    private BigDecimal getRatioFromArray(String currencyFrom, String currencyTo, JSONArray currenciesArray) {
        BigDecimal ratio;
        JSONObject fromObject = LocalJSONConverter.getCurObjectByCurString(currenciesArray,currencyFrom);
        JSONObject toObject = LocalJSONConverter.getCurObjectByCurString(currenciesArray,currencyTo);

        if (!Objects.equals(currencyFrom, BYNString) && !Objects.equals(currencyTo, BYNString)) {
            long scaleFrom = LocalJSONConverter.getScaleFromObject(fromObject);
            ratio = BigDecimal.valueOf(LocalJSONConverter.getRatioFromObject(fromObject));

            long scaleTo = LocalJSONConverter.getScaleFromObject(toObject);
            BigDecimal secondRatio = BigDecimal.valueOf(LocalJSONConverter.getRatioFromObject(toObject));

            ratio = ratio.divide(secondRatio, RoundingMode.HALF_DOWN);
            ratio = ratio.setScale((int) (ratio.scale() + Math.log10(scaleFrom)),RoundingMode.HALF_DOWN)
                    .divide(BigDecimal.valueOf(scaleFrom),RoundingMode.HALF_DOWN);
            ratio = ratio.setScale((int) (ratio.scale() + Math.log10(scaleTo)),RoundingMode.HALF_DOWN)
                    .multiply(BigDecimal.valueOf(scaleTo));

        } else if(Objects.equals(currencyFrom, BYNString)) {
            long scaleTo = LocalJSONConverter.getScaleFromObject(toObject);
            ratio = BigDecimal.valueOf(LocalJSONConverter.getRatioFromObject(toObject));
            ratio = BigDecimal.ONE.setScale(4,RoundingMode.HALF_DOWN).divide(ratio,RoundingMode.HALF_DOWN);
            ratio = ratio.setScale((int) (ratio.scale() + Math.log10(scaleTo)),RoundingMode.HALF_DOWN)
                    .multiply(BigDecimal.valueOf(scaleTo));
        } else {
            long scaleFrom = LocalJSONConverter.getScaleFromObject(fromObject);
            ratio = BigDecimal.valueOf(LocalJSONConverter.getRatioFromObject(fromObject));
            ratio = ratio.setScale((int) (ratio.scale() + Math.log10(scaleFrom)),RoundingMode.HALF_DOWN)
                    .divide(BigDecimal.valueOf(scaleFrom),RoundingMode.HALF_DOWN);
        }
        return ratio;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        BigDecimal ratio;
        if(Objects.equals(currencyFrom, currencyTo)) {
            return BigDecimal.ONE;
        } else {
            ratio = getRatioFromArray(currencyFrom,currencyTo,currencyJSONArray);
        }
        return ratio;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyToList) {
        Map<String,BigDecimal> currenciesHash = new HashMap<>();
        for(String currencyTo : currencyToList) {
            currenciesHash.put(currencyTo,getRatio(currencyFrom,currencyTo));
        }
        return currenciesHash;
    }

    public static void setDir(String dir) {
        CurrencyUpdaterJSON.dir = dir;
    }

}
