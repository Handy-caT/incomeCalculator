package com.incomeCalculator.core.wallet.money.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NBRBAPI implements APIProvider {

    @Override
    public JSONArray getCurrenciesArray() {
        JSONParser jsonParser = new JSONParser();
        JSONArray currenciesWebJSONArray = null;

        String url = "https://www.nbrb.by/api/exrates/rates?periodicity=0";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url).asString();
            String jsonString = httpResponse.getBody();
            currenciesWebJSONArray = (JSONArray) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return currenciesWebJSONArray;
    }

    @Override
    public JSONArray getRatiosArray() {
        JSONParser jsonParser = new JSONParser();
        JSONArray currenciesWebJSONArray = null;

        String url = "https://www.nbrb.by/api/exrates/rates?periodicity=0";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url).asString();
            String jsonString = httpResponse.getBody();
            currenciesWebJSONArray = (JSONArray) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return currenciesWebJSONArray;
    }

    @Override
    public JSONArray getRatiosArray(String date) {

        JSONParser jsonParser = new JSONParser();
        JSONArray result = new JSONArray();

        String url = "https://www.nbrb.by/api/exrates/rates?ondate="+date+"&periodicity=0";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url).asString();
            String jsonString = httpResponse.getBody();
            result = (JSONArray) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public JSONObject getCurrencyUnitObject(long id) {
        JSONParser jsonParser = new JSONParser();
        JSONObject currencyObject = null;

        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=0";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url + id + params).asString();
            String jsonString = httpResponse.getBody();
            currencyObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return currencyObject;
    }

    @Override
    public JSONObject getCurrencyUnitObject(String name) {
        JSONParser jsonParser = new JSONParser();
        JSONObject currencyObject = null;

        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=2";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url + name + params).asString();
            String jsonString = httpResponse.getBody();
            currencyObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return currencyObject;
    }

}
