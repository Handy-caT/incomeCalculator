package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JSONConverter {

    public static final String webIdName = "Cur_ID";
    public static final String webScaleName = "Cur_Scale";
    public static final String webCurName = "Cur_Abbreviation";
    public static final String webRatioName = "Cur_OfficialRate";

    public static final String localIdName = "currencyId";
    public static final String localScaleName = "currencyScale";
    public static final String localCurName = "currencyName";
    public static final String localRatioName = "Ratio";

    public static List<String> getCurStringList(JSONArray currencyArray) {
        List<String> curList = new LinkedList<>();
        for(Object object : currencyArray) {
            JSONObject currencyObject = (JSONObject) object;
            String currencyString = (String) currencyObject.get(webCurName);
            curList.add(currencyString);
        }
        return curList;
    }

    public static JSONObject getCurObjectByCurString(JSONArray currencyArray, String currencyString) {
        JSONObject currencyObject = null;
        for(Object object : currencyArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get(webCurName);
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }
        return currencyObject;
    }

    public static JSONObject convertWebCurObjectToLocal(JSONObject currencyObject) {
        long currencyId = (long)currencyObject.get(webIdName);
        long currencyScale = (long)currencyObject.get(webScaleName);
        String currencyString = (String) currencyObject.get(webCurName);
        double ratio = (double) currencyObject.get(webRatioName);

        JSONObject localCurrencyObject = new JSONObject();
        localCurrencyObject.put(localCurName,currencyString);
        localCurrencyObject.put(localIdName,currencyId);
        localCurrencyObject.put(localScaleName,currencyScale);
        if(ratio != 0) localCurrencyObject.put(localRatioName,ratio);

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
