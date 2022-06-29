package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb;

import com.incomeCalculator.core.wallet.money.currencyUnit.StrictCurrencyUnit;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import com.incomeCalculator.core.wallet.money.util.APIProvider;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;

public class CurrencyUnitWebStorage implements CurrencyUnitStorage {

    private APIProvider api;

    protected CurrencyUnitWebStorage() {

    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId)  {

        JSONObject currencyObject = api.getCurrencyUnitObject(currencyId);

        String currencyString = WebJSONConverter.getNameFromObject(currencyObject);
        long scale = WebJSONConverter.getScaleFromObject(currencyObject);

        return new StrictCurrencyUnit(currencyString,currencyId,scale);
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString)  {

        JSONObject currencyObject = api.getCurrencyUnitObject(currencyString);

        long currencyId = WebJSONConverter.getIdFromObject(currencyObject);
        long scale = WebJSONConverter.getScaleFromObject(currencyObject);

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

    public void setApi(APIProvider api) {
        this.api = api;
    }

}
