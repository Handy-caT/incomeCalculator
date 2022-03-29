package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CurrencyUnitJSONStorage implements CurrencyUnitStorage {

    private String jsonPathString = null;

    JSONArray currencyJSONArray;

    public CurrencyUnitJSONStorage() throws IOException {
        CurrencyUnitJSONStorageBuilderSingleton builder = CurrencyUnitJSONStorageBuilderSingleton.getInstance();
        builder.reset();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "temp/currencyUnitArray";

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    public CurrencyUnitJSONStorage(List<String> buildingPlan) throws IOException {
        CurrencyUnitJSONStorageBuilderSingleton builder = CurrencyUnitJSONStorageBuilderSingleton.getInstance();
        builder.reset();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "temp/currencyUnitArray";

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }

    public CurrencyUnitJSONStorage(String jsonPathString) throws IOException, ParseException {
        this.jsonPathString = jsonPathString;

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonPathString);
        currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();
    }

    @Override
    public CurrencyUnit getCurrencyUnitByCurrencyString(String currencyString) {
        JSONObject currencyObject = null;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get("currencyName");
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }
        BigDecimal currencyId = BigDecimal.valueOf((long)currencyObject.get("currencyId"));
        BigDecimal currencyScale = BigDecimal.valueOf((long)currencyObject.get("currencyScale"));

        return new CurrencyUnit(currencyString,currencyId,currencyScale);
    }

    @Override
    public CurrencyUnit getCurrencyUnitByCurrencyID(BigDecimal currencyId) {
        JSONObject currencyObject = null;
        for(Object object : currencyJSONArray) {
            currencyObject = (JSONObject) object;
            BigDecimal tempCurrencyId = BigDecimal.valueOf((long)currencyObject.get("currencyId"));
            if(Objects.equals(currencyId, tempCurrencyId)) break;
        }
        String currencyString = (String) currencyObject.get("currencyName");
        BigDecimal currencyScale = BigDecimal.valueOf((long)currencyObject.get("currencyScale"));

        return new CurrencyUnit(currencyString,currencyId,currencyScale);
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
