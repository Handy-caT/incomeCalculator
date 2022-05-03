package wallet.money.util;

import org.json.simple.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WebApiJSON {

    private static JSONArray currenciesWebJSONArray;
    private static WebApiJSON instance;

    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private static String dateString;

    private static APIProvider api;

    private WebApiJSON() {
        currenciesWebJSONArray = api.getRatiosArray();

        Date date = new Date();
        dateString = webFormatter.format(date);
    }

    public JSONArray getCurrenciesJSONArray() {
        return currenciesWebJSONArray;
    }

    public void Update() {
        Date date = new Date();
        dateString = webFormatter.format(date);

        instance = new WebApiJSON();
    }

    public boolean needToUpdate() {
        Date date = new Date();
        String nowDate = webFormatter.format(date);
        return !Objects.equals(nowDate, dateString);
    }

    public static WebApiJSON getInstance() {
        if(instance == null) {
            if(api == null) api = new NBRBAPI();
            instance = new WebApiJSON();
        }
        return instance;
    }

    public JSONArray getCurrenciesJSONArrayOnDate(String dateString) {
        return api.getRatiosArray(dateString);
    }

    public static void setApi(APIProvider api) {
        WebApiJSON.api = api;
    }

}