package tests.moneyTests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wallet.money.APIProvider;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Objects;

public class TestAPI implements APIProvider {

    String jsonPathString;

    public TestAPI(String jsonPath) {
        jsonPathString = jsonPath;
    }

    @Override
    public JSONArray getCurrenciesArray() {
        return null;
    }

    @Override
    public JSONArray getRatiosArray() {
        JSONArray currencyJSONArray = null;
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader fileReader = new FileReader(jsonPathString);
            currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyJSONArray;
    }

    @Override
    public JSONArray getRatiosArray(String date) {
        return null;
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
