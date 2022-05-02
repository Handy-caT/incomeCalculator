package wallet.money.currencyUnit.currencyUnitJSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import wallet.money.currencyUnit.StrictCurrencyUnit;

import java.io.*;
import java.math.BigDecimal;
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
            String tempCurrencyString = (String) currencyObject.get("currencyName");
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }
        long currencyId = (long)currencyObject.get("currencyId");
        long currencyScale = (long)currencyObject.get("currencyScale");

        return new StrictCurrencyUnit(currencyString,currencyId,currencyScale);
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId) {
        JSONObject currencyObject = null;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            BigDecimal tempCurrencyId = BigDecimal.valueOf((long)currencyObject.get("currencyId"));
            if(Objects.equals(currencyId, tempCurrencyId)) break;
        }
        String currencyString = (String) currencyObject.get("currencyName");
        long currencyScale = (long)currencyObject.get("currencyScale");

        return new StrictCurrencyUnit(currencyString,currencyId,currencyScale);
    }

    @Override
    public boolean isCurrencyExists(String currencyString) {
        JSONObject currencyObject;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get("currencyName");
            if(Objects.equals(tempCurrencyString, currencyString)) return true;
        }
        return false;
    }

}
