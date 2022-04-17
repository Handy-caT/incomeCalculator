package wallet.money.currencyUnit;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;

public class CurrencyUnitWebStorage implements CurrencyUnitStorage {

    private static CurrencyUnitWebStorage instance;

    public static CurrencyUnitWebStorage getInstance() {
        if(instance == null) {
            instance = new CurrencyUnitWebStorage();
        }
        return instance;
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId)  {
        JSONParser jsonParser = new JSONParser();
        JSONObject currencyObject = null;

        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=0";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url + currencyId + params).asString();
            String jsonString = httpResponse.getBody();
            currencyObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        String currencyString = (String) currencyObject.get("Cur_Abbreviation");
        long scale = (long)currencyObject.get("Cur_Scale");

        return new StrictCurrencyUnit(currencyString,currencyId,scale);
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject currencyObject = null;

        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=2";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url + currencyString + params).asString();
            String jsonString = httpResponse.getBody();
            currencyObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        long currencyId = (long)currencyObject.get("Cur_ID");
        long scale = (long)currencyObject.get("Cur_Scale");

        return new StrictCurrencyUnit(currencyString,currencyId,scale);
    }

    @Override
    public boolean isCurrencyExists(String currencyString) {
        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=2";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url + currencyString + params).asString();
        } catch (UnirestException e) {
            return false;
        }
        return true;
    }

}
