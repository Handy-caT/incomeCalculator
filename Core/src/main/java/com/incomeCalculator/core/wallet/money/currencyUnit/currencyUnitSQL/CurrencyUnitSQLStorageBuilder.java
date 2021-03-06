package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorageBuilder;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
        return WebJSONConverter.getCurStringList(currenciesWebJSONArray);
    }

    @Override
    public void buildCurrencyUnit(String currencyString) {

        JSONObject currencyObject = WebJSONConverter.getCurObjectByCurString(currenciesWebJSONArray,currencyString);

        if(currencyObject != null) {
            long currencyId = WebJSONConverter.getIdFromObject(currencyObject);
            long currencyScale = WebJSONConverter.getScaleFromObject(currencyObject);
            try {
                PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO " + tableName +
                        " (currencyId, currencyName, currencyScale) VALUES (?, ?, ?)");
                preparedStatement.setLong(1, currencyId);
                preparedStatement.setString(2, currencyString);
                preparedStatement.setLong(3, currencyScale);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Can't find " + currencyString + " currency");
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

    public static CurrencyUnitSQLStorageBuilder getInstance(String tableName, Connection dbConnection) {
        CurrencyUnitSQLStorageBuilder.tableName = tableName;
        CurrencyUnitSQLStorageBuilder.dbConnection = dbConnection;

        if(instance == null) {
            instance = new CurrencyUnitSQLStorageBuilder();
        }
        return instance;
    }

}
