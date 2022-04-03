package wallet.money;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CurrencyUpdaterWeb implements CurrencyUpdaterProvider {

    private static CurrencyUpdaterWeb instance;

    private JSONObject getCurrencyJSONFromWeb(String curName) throws ParseException, UnirestException {
        JSONParser jsonParser = new JSONParser();

        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=2";
        HttpResponse<String> httpResponse = Unirest.get(url + curName + params).asString();
        String jsonString = httpResponse.getBody();

        return (JSONObject) jsonParser.parse(jsonString);
    }
    private JSONObject getCurrencyJSONFromWebOnDate(String curName, Date date) throws ParseException, UnirestException {
        JSONParser jsonParser = new JSONParser();

        String url = "https://www.nbrb.by/api/exrates/rates/";
        String dateString = date.toString();
        String params = "parammode=2";
        HttpResponse<String> httpResponse = Unirest.get(url + curName +"?"+ dateString + "&" + params).asString();
        String jsonString = httpResponse.getBody();

        return (JSONObject) jsonParser.parse(jsonString);
    }

    private JSONArray getCurrenciesJSONArrayFromWeb() throws UnirestException, ParseException {
        JSONParser jsonParser = new JSONParser();

        String url = "https://www.nbrb.by/api/exrates/rates?periodicity=0";
        HttpResponse<String> httpResponse = Unirest.get(url).asString();
        String jsonString = httpResponse.getBody();

        return (JSONArray) jsonParser.parse(jsonString);
    }
    private JSONObject getCurrencyJSONFromJSONArray(JSONArray currenciesArray, String currencyName) {
        JSONObject currencyJSONObject;
        JSONObject result = null;

        for( Object currencyObject : currenciesArray) {
           currencyJSONObject = (JSONObject) currencyObject;
           String tempCurrencyName = (String) currencyJSONObject.get("Cur_Abbreviation");
           if(Objects.equals(tempCurrencyName, currencyName)) {
               result = currencyJSONObject;
               break;
           }
        }
        return result;
    }

    private BigDecimal findRatioOfCurrencies(JSONObject currencyFrom, JSONObject currencyTo) {
        BigDecimal ratioFirst = BigDecimal.valueOf((double) currencyFrom.get("Cur_OfficialRate"));
        BigDecimal ratioSecond = BigDecimal.valueOf((double) currencyTo.get("Cur_OfficialRate"));
        return  ratioFirst.divide(ratioSecond, RoundingMode.DOWN);
    }

    public static CurrencyUpdaterWeb getInstance() {
        if(instance == null) {
            instance = new CurrencyUpdaterWeb();
        }
        return instance;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo)  {

        if(Objects.equals(currencyFrom, currencyTo)) {
            return BigDecimal.ONE;
        } else {
            BigDecimal ratio = null;
            try {
                JSONObject currencyObject= getCurrencyJSONFromWeb(currencyFrom);
                BigDecimal ratioFirst = BigDecimal.valueOf((double) currencyObject.get("Cur_OfficialRate"));

                if(!Objects.equals(currencyFrom, "BYN")) {
                    JSONObject secondCurrencyObject = getCurrencyJSONFromWeb(currencyTo);
                    ratio = findRatioOfCurrencies(currencyObject,secondCurrencyObject);
                } else if(!Objects.equals(currencyTo, "BYN")) {
                    JSONObject secondCurrencyObject = getCurrencyJSONFromWeb(currencyFrom);
                    ratio = BigDecimal.valueOf((double) secondCurrencyObject.get("Cur_OfficialRate"));
                    ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
                } else ratio = ratioFirst;

            } catch (UnirestException | ParseException e) {
                e.printStackTrace();
            }
            return ratio;
        }
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

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        BigDecimal ratio = null;
        try {
            JSONObject currencyObject = getCurrencyJSONFromWebOnDate(currencyFrom,date);
            BigDecimal ratioFirst = BigDecimal.valueOf((double) currencyObject.get("Cur_OfficialRate"));

            if(!Objects.equals(currencyTo, "BYN")) {
                currencyObject = getCurrencyJSONFromWebOnDate(currencyTo,date);
                BigDecimal ratioSecond = BigDecimal.valueOf((double) currencyObject.get("Cur_OfficialRate"));
                ratio =  ratioFirst.divide(ratioSecond, RoundingMode.DOWN);
            } else ratio = ratioFirst;

        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return ratio;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyToList) {
        Map<String,BigDecimal> currenciesHash = new HashMap<>();

        try {
            JSONArray currenciesArray = getCurrenciesJSONArrayFromWeb();
            JSONObject currencyFromObject = getCurrencyJSONFromJSONArray(currenciesArray,currencyFrom);

            for(String currencyTo : currencyToList) {
                if(Objects.equals(currencyTo, currencyFrom)) {
                    currenciesHash.put(currencyTo,BigDecimal.ONE);
                } else if(Objects.equals("BYN", currencyTo) ) {
                    JSONObject currencyObject = getCurrencyJSONFromJSONArray(currenciesArray,currencyFrom);
                    BigDecimal ratio = BigDecimal.valueOf((double) currencyObject.get("Cur_OfficialRate"));
                    ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
                    currenciesHash.put(currencyTo,ratio);
                } else if(Objects.equals("BYN", currencyFrom)) {
                    JSONObject currencyObject = getCurrencyJSONFromJSONArray(currenciesArray,currencyTo);
                    BigDecimal ratio = BigDecimal.valueOf((double) currencyObject.get("Cur_OfficialRate"));
                    currenciesHash.put(currencyTo,ratio);
                } else {
                    JSONObject currencyObject = getCurrencyJSONFromJSONArray(currenciesArray,currencyTo);
                    BigDecimal ratio = findRatioOfCurrencies(currencyFromObject,currencyObject);
                    currenciesHash.put(currencyTo,ratio);
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return currenciesHash;
    }

}


