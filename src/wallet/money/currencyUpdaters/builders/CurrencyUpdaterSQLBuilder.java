package wallet.money.currencyUpdaters.builders;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.money.WebApiJSON;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CurrencyUpdaterSQLBuilder implements CurrencyUpdaterBuilder {

    private String tableName;
    private Connection dbConnection;
    private String dateString;

    private  JSONArray currenciesWebJSONArray;

    public CurrencyUpdaterSQLBuilder(String tableName, Connection dbConnection, String dateString) {

        this.dateString = dateString;
        this.dbConnection = dbConnection;
        this.tableName = tableName;

        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        currenciesWebJSONArray = webApiJSON.getCurrenciesWebJSONArrayOnDate(dateString);
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
        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();
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
                    dateString + " (currencyFrom , currencyScale, ratio) VALUES (?, ?, ?)");
            preparedStatement.setString(1,currencyString);
            preparedStatement.setLong(2,currencyScale);
            preparedStatement.setBigDecimal(3,ratio);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
