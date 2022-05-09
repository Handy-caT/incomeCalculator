package wallet.money.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Date;

public interface APIProvider {

    JSONArray getCurrenciesArray();
    JSONArray getRatiosArray();
    JSONArray getRatiosArray(String date);
    JSONObject getCurrencyUnitObject(long id);
    JSONObject getCurrencyUnitObject(String name);

}
