package wallet.money.currencyUpdater.builders;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CurrencyUpdaterSQLBuilder implements CurrencyUpdaterBuilder {

    private static CurrencyUpdaterSQLBuilder instance;
    private static String tableName;
    private static Connection dbConnection;

    private static JSONArray currenciesWebJSONArray;

    private CurrencyUpdaterSQLBuilder() {
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
    }

    public List<String> getBuildPlan() {
        List<String> buildPlanList = new LinkedList<>();
        for(Object object : currenciesWebJSONArray) {
            JSONObject currencyObject = (JSONObject) object;
            String currencyString = (String) currencyObject.get("Cur_Abbreviation");
            buildPlanList.add(currencyString);
        }
        return buildPlanList;
    }

    @Override
    public void reset() {
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
    }

    @Override
    public void buildCurrency(String currencyString) {
        JSONObject currencyObject = null;
        for(Object object : currenciesWebJSONArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get("Cur_Abbreviation");
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }

        BigDecimal ratio = BigDecimal.valueOf((double)currencyObject.get("Cur_OfficialRate"));
        long currencyScale = (long)currencyObject.get("Cur_Scale");
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO " + tableName +
                    " (currencyFrom , currencyScale, ratio) VALUES (?, ?, ?)");
            preparedStatement.setString(1,currencyString);
            preparedStatement.setLong(2,currencyScale);
            preparedStatement.setBigDecimal(3,ratio);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CurrencyUpdaterSQLBuilder getInstance(String tableName, Connection dbConnection) throws SQLException {
        CurrencyUpdaterSQLBuilder.tableName = tableName;
        CurrencyUpdaterSQLBuilder.dbConnection = dbConnection;

        if(instance == null) {
            instance = new CurrencyUpdaterSQLBuilder();
        }
        return instance;
    }
}
