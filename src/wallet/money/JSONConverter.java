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
            String currencyString = (String) currencyObject.get("Cur_Abbreviation");
            curList.add(currencyString);
        }
        return curList;
    }

    public static JSONObject getCurObjectByCurString(JSONArray currencyArray, String currencyString) {
        JSONObject currencyObject = null;
        for(Object object : currencyArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get("Cur_Abbreviation");
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }
        return currencyObject;
    }

    public static JSONObject convertWebCurObjectToLocal(JSONObject currencyObject) {
        long currencyId = (long)currencyObject.get("Cur_ID");
        long currencyScale = (long)currencyObject.get("Cur_Scale");
        String currencyString = (String) currencyObject.get("Cur_Abbreviation");
        Object ratioObject = currencyObject.get("Cur_OfficialRate");

        JSONObject localCurrencyObject = new JSONObject();
        localCurrencyObject.put("currencyName",currencyString);
        localCurrencyObject.put("currencyId",currencyId);
        localCurrencyObject.put("currencyScale",currencyScale);
        if(ratioObject != null) localCurrencyObject.put("Ratio",currencyScale);

        return localCurrencyObject;
    }

}
