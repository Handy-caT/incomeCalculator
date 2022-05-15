package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON;

import com.incomeCalculator.core.wallet.PropertiesStorage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import com.incomeCalculator.core.wallet.money.currencyUnit.StrictCurrencyUnit;
import com.incomeCalculator.core.wallet.money.util.LocalJSONConverter;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class CurrencyUnitJSONStorage implements CurrencyUnitStorage {

    private static String jsonPathString;

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private static JSONArray currencyJSONArray;

    public static final String propertyName = "CurrencyUnitStoragePath";

    protected CurrencyUnitJSONStorage() throws IOException {
        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUnitStorage.json";
        propertiesStorage.addProperty(propertyName,jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    protected CurrencyUnitJSONStorage(List<String> buildingPlan) throws IOException {
        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUnitStorage.json";
        propertiesStorage.addProperty(propertyName,jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    protected CurrencyUnitJSONStorage(String jsonPathString) throws IOException, ParseException {
        CurrencyUnitJSONStorage.jsonPathString = jsonPathString;

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonPathString);
        currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString) {
        JSONObject currencyObject = null;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = LocalJSONConverter.getNameFromObject(currencyObject);
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }
        if(currencyObject != null) {
            long currencyId = LocalJSONConverter.getIdFromObject(currencyObject);
            long currencyScale = LocalJSONConverter.getScaleFromObject(currencyObject);

            return new StrictCurrencyUnit(currencyString, currencyId, currencyScale);
        } else {
            throw new IllegalArgumentException("Currency with " + currencyString + " nome not found");
        }
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId) {
        JSONObject currencyObject = null;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            long tempCurrencyId = LocalJSONConverter.getIdFromObject(currencyObject);
            if(Objects.equals(currencyId, tempCurrencyId)) break;
        }
        if(currencyObject != null) {
            String currencyString = LocalJSONConverter.getNameFromObject(currencyObject);
            long currencyScale = LocalJSONConverter.getScaleFromObject(currencyObject);

            return new StrictCurrencyUnit(currencyString, currencyId, currencyScale);
        } else {
            throw new IllegalArgumentException("Currency with " + currencyId + " id not found");
        }
    }

    @Override
    public boolean isCurrencyExists(String currencyString) {
        JSONObject currencyObject;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = LocalJSONConverter.getNameFromObject(currencyObject);
            if(Objects.equals(tempCurrencyString, currencyString)) return true;
        }
        return false;
    }

}
