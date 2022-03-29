package wallet.money;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CurrencyUnitJSONStorageBuilder implements CurrencyUnitStorageBuilder{

    private JSONArray currenciesWebJSONArray;
    private JSONArray currenciesJSONArray;

    public CurrencyUnitJSONStorageBuilder() {
        JSONParser jsonParser = new JSONParser();

        String url = "https://www.nbrb.by/api/exrates/currencies";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url).asString();
            String jsonString = httpResponse.getBody();
            currenciesWebJSONArray = (JSONArray) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        currenciesJSONArray = new JSONArray();
    }

    public List<String> getBuildPlan() {
        List<String> buildPlanList = new LinkedList<>();
        for(Object object : currenciesWebJSONArray) {
            JSONObject currencyObject = (JSONObject) object;
            String currencyString = (String) currencyObject.get("Cur_Abbreviation");
            buildPlanList.add(currencyString);
        }
        return buildPlanList;
    }

    @Override
    public void buildCurrencyUnit(String currencyString) {
        JSONObject currencyObject = null;
        for(Object object : currenciesWebJSONArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get("Cur_Abbreviation");
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }

        BigDecimal currencyId = BigDecimal.valueOf((long)currencyObject.get("Cur_ID"));
        BigDecimal currencyScale = BigDecimal.valueOf((long)currencyObject.get("Cur_Scale"));

        JSONObject localCurrencyObject = new JSONObject();
        localCurrencyObject.put("currencyName",currencyString);
        localCurrencyObject.put("currencyId",currencyId.longValue());
        localCurrencyObject.put("currencyScale",currencyScale.longValue());

        currenciesJSONArray.add(localCurrencyObject);
    }

    public JSONArray getResult() {
        return currenciesJSONArray;
    }

}
