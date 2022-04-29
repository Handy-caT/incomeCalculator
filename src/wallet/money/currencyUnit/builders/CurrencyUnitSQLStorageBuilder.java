package wallet.money.currencyUnit.builders;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.money.WebApiJSON;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CurrencyUnitSQLStorageBuilder implements CurrencyUnitStorageBuilder {

    private static CurrencyUnitSQLStorageBuilder instance;
    private static String tableName;
    private static Connection dbConnection;

    private static JSONArray currenciesWebJSONArray;

    private CurrencyUnitSQLStorageBuilder() {
        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();
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
    public void buildCurrencyUnit(String currencyString) {
        JSONObject currencyObject = null;
        for(Object object : currenciesWebJSONArray) {
            currencyObject = (JSONObject) object;
            String tempCurrencyString = (String) currencyObject.get("Cur_Abbreviation");
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }

        long currencyId = (long)currencyObject.get("Cur_ID");
        long currencyScale = (long)currencyObject.get("Cur_Scale");
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO " + tableName +
                    " (currencyId, currencyName, currencyScale) VALUES (?, ?, ?)");
            preparedStatement.setLong(1,currencyId);
            preparedStatement.setString(2,currencyString);
            preparedStatement.setLong(3,currencyScale);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
        try {
            Statement statement = dbConnection.createStatement();
            statement.execute("DROP TABLE " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CurrencyUnitSQLStorageBuilder getInstance(String tableName, Connection dbConnection) throws SQLException {
        CurrencyUnitSQLStorageBuilder.tableName = tableName;
        CurrencyUnitSQLStorageBuilder.dbConnection = dbConnection;

        if(instance == null) {
            instance = new CurrencyUnitSQLStorageBuilder();
        }
        return instance;
    }

}
