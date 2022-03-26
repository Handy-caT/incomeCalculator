package wallet.money;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Objects;

public class CurrencyUpdaterWeb implements CurrencyUpdaterProvider {

    private JSONObject getCurrencyJSONFromWeb(String curName) throws ParseException, UnirestException {
        JSONParser jsonParser = new JSONParser();

        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=2";
        HttpResponse<String> httpResponse = Unirest.get(url + curName + params).asString();
        String jsonString = httpResponse.getBody();

        return (JSONObject) jsonParser.parse(jsonString);
    }

    @Override
    public HashMap<String, BigDecimal> getCurrencyHash(String currencyName) {
        return null;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo)  {

        BigDecimal ratio = null;
        try {
            JSONObject currencyObject= getCurrencyJSONFromWeb(currencyFrom);
            BigDecimal ratioFirst = BigDecimal.valueOf((double) currencyObject.get("Cur_OfficialRate"));

            if(!Objects.equals(currencyTo, "BYN")) {
                currencyObject= getCurrencyJSONFromWeb(currencyTo);
                BigDecimal ratioSecond = BigDecimal.valueOf((double) currencyObject.get("Cur_OfficialRate"));
                ratio =  ratioFirst.divide(ratioSecond, RoundingMode.DOWN);
            } else ratio = ratioFirst;

        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return ratio;
    }

    @Override
    public BigDecimal getCurScale(String currencyName) {
        BigDecimal scale = null;
        try{
            JSONObject currencyObject = getCurrencyJSONFromWeb(currencyName);
             scale = BigDecimal.valueOf((long) currencyObject.get("Cur_Scale"));
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return scale;
    }

    @Override
    public BigDecimal getCurID(String currencyName) {
        BigDecimal id = null;
        try{
            JSONObject currencyObject = getCurrencyJSONFromWeb(currencyName);
            id = BigDecimal.valueOf((long) currencyObject.get("Cur_ID"));
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return id;
    }
}
