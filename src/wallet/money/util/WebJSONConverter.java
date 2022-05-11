package wallet.money.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class WebJSONConverter  extends JSONConverter {

    public static final String webIdName = "Cur_ID";
    public static final String webScaleName = "Cur_Scale";
    public static final String webCurName = "Cur_Abbreviation";
    public static final String webRatioName = "Cur_OfficialRate";

    public static List<String> getCurStringList(JSONArray currencyArray) {
        return getCurStringList(currencyArray,webCurName);
    }

    public static JSONObject getCurObjectByCurString(JSONArray currencyArray, String currencyString) {
        return getCurObjectByCurString(currencyArray,currencyString,webCurName);
    }

    public static JSONObject convertWebCurObjectToLocal(JSONObject currencyObject) {
        long currencyId = (long)currencyObject.get(webIdName);
        long currencyScale = (long)currencyObject.get(webScaleName);
        String currencyString = (String) currencyObject.get(webCurName);
        double ratio = (double) currencyObject.get(webRatioName);

        JSONObject localCurrencyObject = new JSONObject();
        localCurrencyObject.put(LocalJSONConverter.localCurName,currencyString);
        localCurrencyObject.put(LocalJSONConverter.localIdName,currencyId);
        localCurrencyObject.put(LocalJSONConverter.localScaleName,currencyScale);
        if(ratio != 0) localCurrencyObject.put(LocalJSONConverter.localRatioName,ratio);

        return localCurrencyObject;
    }


    public static long getScaleFromObject(JSONObject currencyObject) {
        return (long) currencyObject.get(webScaleName);
    }

    public static double getRatioFromObject(JSONObject currencyObject) {
        return (double) currencyObject.get(webRatioName);
    }

    public static String getNameFromObject(JSONObject currencyObject) {
        return (String) currencyObject.get(webCurName);
    }

    public static long getIdFromObject(JSONObject currencyObject) {
        return (long) currencyObject.get(webIdName);
    }

}
