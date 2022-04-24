package wallet.money.currencyUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.builders.CurrencyUnitJSONStorageBuilder;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CurrencyUnitJSONStorage implements CurrencyUnitStorage {

    private static CurrencyUnitJSONStorage instance;
    private static String jsonPathString;

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private static JSONArray currencyJSONArray;

    private CurrencyUnitJSONStorage() throws IOException {
        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUnitStorage.json";
        propertiesStorage.addProperty("CurrencyUnitStoragePath",jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    private CurrencyUnitJSONStorage(List<String> buildingPlan) throws IOException {
        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUnitStorage.json";
        propertiesStorage.addProperty("CurrencyUnitStoragePath",jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    private CurrencyUnitJSONStorage(String jsonPathString) throws IOException, ParseException {
        CurrencyUnitJSONStorage.jsonPathString = jsonPathString;

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonPathString);
        currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();
    }

    private static CurrencyUnitJSONStorage createInstance() throws IOException, ParseException {

        String jsonPathString = (String) propertiesStorage.getProperty("CurrencyUnitStoragePath");
        if(jsonPathString == null) {
            return new CurrencyUnitJSONStorage();
        } else {
            return new CurrencyUnitJSONStorage(jsonPathString);
        }
    }

    public static CurrencyUnitJSONStorage getInstance() throws IOException, ParseException {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
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
