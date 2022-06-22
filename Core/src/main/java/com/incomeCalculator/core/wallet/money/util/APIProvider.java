package com.incomeCalculator.core.wallet.money.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface APIProvider {

    JSONArray getCurrenciesArray();
    JSONArray getRatiosArray();
    JSONArray getRatiosArray(String date);
    JSONObject getCurrencyUnitObject(long id);
    JSONObject getCurrencyUnitObject(String name);

}
