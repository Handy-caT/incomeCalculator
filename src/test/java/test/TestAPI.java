package test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wallet.money.util.APIProvider;

import java.io.FileReader;
import java.util.Objects;

public class TestAPI implements APIProvider {

    final String jsonPathString;
    String onDateString;

    public TestAPI(String jsonPath) {
        jsonPathString = jsonPath;
    }
    public void setonDateString(String path) {
        onDateString = path;
    }

    @Override
    public JSONArray getCurrenciesArray() {
        return null;
    }

    @Override
    public JSONArray getRatiosArray() {
        return getJsonArrayFromFile(jsonPathString);
    }

    @Override
    public JSONArray getRatiosArray(String date) {
        return getJsonArrayFromFile(onDateString);
    }

    private JSONArray getJsonArrayFromFile(String onDateString) {
        JSONArray currencyJSONArray = null;
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader fileReader = new FileReader(onDateString);
            currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyJSONArray;
    }

    @Override
    public JSONObject getCurrencyUnitObject(long id) {
        JSONArray currenciesArray = getRatiosArray();
        JSONObject currencyObject = null;
        for(Object object : currenciesArray) {
            currencyObject = (JSONObject) object;
            long tempCurrencyId = (long) currencyObject.get("Cur_ID");
            if(Objects.equals(id, tempCurrencyId)) break;
        }
        return currencyObject;
    }

    @Override
    public JSONObject getCurrencyUnitObject(String name) {
        JSONArray currenciesArray = getRatiosArray();
        JSONObject currencyObject = null;
        for(Object object : currenciesArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get("Cur_Abbreviation");
            if(Objects.equals(tempCurrencyString, name)) break;
        }
        return currencyObject;
    }

}
