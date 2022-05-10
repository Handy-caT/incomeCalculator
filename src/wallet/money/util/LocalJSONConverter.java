package wallet.money.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class LocalJSONConverter extends JSONConverter {

    public static final String localIdName = "currencyId";
    public static final String localScaleName = "currencyScale";
    public static final String localCurName = "currencyName";
    public static final String localRatioName = "Ratio";

    public static List<String> getCurStringList(JSONArray currencyArray) {
        return getCurStringList(currencyArray,localCurName);
    }

    public static JSONObject getCurObjectByCurString(JSONArray currencyArray, String currencyString) {
        return getCurObjectByCurString(currencyArray,currencyString,localCurName);
    }


    public static long getScaleFromObject(JSONObject currencyObject) {
        return (long) currencyObject.get(localScaleName);
    }

    public static double getRatioFromObject(JSONObject currencyObject) {
        return (double) currencyObject.get(localRatioName);
    }

    public static String getNameFromObject(JSONObject currencyObject) {
        return (String) currencyObject.get(localCurName);
    }

    public static long getIdFromObject(JSONObject currencyObject) {
        return (long) currencyObject.get(localIdName);
    }

}
