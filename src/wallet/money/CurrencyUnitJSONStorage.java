package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class CurrencyUnitJSONStorage implements CurrencyUnitStorage {

    private static CurrencyUnitJSONStorage instance;
    private static String jsonPathString;

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
        addJsonPathToProperties(jsonPathString);

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
        addJsonPathToProperties(jsonPathString);

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
        FileInputStream fis = new FileInputStream("properties/json.properties");
        Properties properties = new Properties();
        properties.load(fis);

        String jsonPathString = (String) properties.get("CurrencyUnitStoragePath");
        if(jsonPathString == null) {
            return new CurrencyUnitJSONStorage();
        } else {
            return new CurrencyUnitJSONStorage(jsonPathString);
        }
    }
    private static void addJsonPathToProperties(String jsonPathString) throws IOException {
        FileInputStream fis = new FileInputStream("properties/json.properties");
        Properties properties = new Properties();
        properties.load(fis);

        properties.put("CurrencyUnitStoragePath",jsonPathString);
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
        BigDecimal currencyId = BigDecimal.valueOf((long)currencyObject.get("currencyId"));
        BigDecimal currencyScale = BigDecimal.valueOf((long)currencyObject.get("currencyScale"));

        return new StrictCurrencyUnit(currencyString,currencyId,currencyScale);
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(BigDecimal currencyId) {
        JSONObject currencyObject = null;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            BigDecimal tempCurrencyId = BigDecimal.valueOf((long)currencyObject.get("currencyId"));
            if(Objects.equals(currencyId, tempCurrencyId)) break;
        }
        String currencyString = (String) currencyObject.get("currencyName");
        BigDecimal currencyScale = BigDecimal.valueOf((long)currencyObject.get("currencyScale"));

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
