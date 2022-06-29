package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON;

import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorageBuilder;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;


public class CurrencyUnitJSONStorageBuilder implements CurrencyUnitStorageBuilder {

    private static CurrencyUnitJSONStorageBuilder instance;

    private static JSONArray currenciesWebJSONArray;
    private static JSONArray currenciesJSONArray;

    private CurrencyUnitJSONStorageBuilder() {
        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();

        currenciesJSONArray = new JSONArray();
    }

    public static CurrencyUnitJSONStorageBuilder getInstance() {
        if(instance == null) {
            instance = new CurrencyUnitJSONStorageBuilder();
        }
        return instance;
    }

    public List<String> getBuildPlan() {
        return WebJSONConverter.getCurStringList(currenciesWebJSONArray);
    }

    @Override
    public void reset() {
        currenciesJSONArray = new JSONArray();
    }

    @Override
    public void buildCurrencyUnit(String currencyString) {
        JSONObject currencyObject = WebJSONConverter.getCurObjectByCurString(currenciesWebJSONArray,currencyString);
        JSONObject localCurrencyObject = WebJSONConverter.convertWebCurObjectToLocal(currencyObject);

        currenciesJSONArray.add(localCurrencyObject);
    }

    public JSONArray getResult() {
        return currenciesJSONArray;
    }

}
