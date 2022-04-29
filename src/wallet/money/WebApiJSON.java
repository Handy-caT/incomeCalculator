package wallet.money;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WebApiJSON {

    private static JSONArray currenciesWebJSONArray;
    private static WebApiJSON instance;

    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private String dateString;

    private WebApiJSON() {
        JSONParser jsonParser = new JSONParser();

        String url = "https://www.nbrb.by/api/exrates/rates?periodicity=0";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url).asString();
            String jsonString = httpResponse.getBody();
            currenciesWebJSONArray = (JSONArray) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

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
            instance = new WebApiJSON();
        }
        return instance;
    }

    public JSONArray getCurrenciesWebJSONArrayOnDate(String dateString) {
        JSONParser jsonParser = new JSONParser();
        JSONArray result = new JSONArray();

        String url = "https://www.nbrb.by/api/exrates/rates?ondate="+dateString+"&periodicity=0";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url).asString();
            String jsonString = httpResponse.getBody();
            result = (JSONArray) jsonParser.parse(jsonString);
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

}
