package wallet.money.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class JSONConverter {

    protected static List<String> getCurStringList(JSONArray currencyArray,String parameterName) {
        List<String> curList = new LinkedList<>();
        for(Object object : currencyArray) {
            JSONObject currencyObject = (JSONObject) object;
            String currencyString = (String) currencyObject.get(parameterName);
            curList.add(currencyString);
        }
        return curList;
    }

    protected static JSONObject getCurObjectByCurString(JSONArray currencyArray,
                                                        String currencyString, String parameterName) {
        JSONObject currencyObject = null;
        for(Object object : currencyArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get(parameterName);
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }
        return currencyObject;
    }

}
