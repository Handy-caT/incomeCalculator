package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL;

import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterBuilder;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CurrencyUpdaterSQLBuilder implements CurrencyUpdaterBuilder {

    private final String tableName;
    private final Connection dbConnection;
    private final String dateString;

    private  JSONArray currenciesWebJSONArray;

    public CurrencyUpdaterSQLBuilder(String tableName, Connection dbConnection, String dateString) {

        this.dateString = dateString;
        this.dbConnection = dbConnection;
        this.tableName = tableName;

        Date date = new Date();
        String currentDateString = DateFormatter.sqlFormat(date);

        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(currentDateString.equals(dateString)) {
            currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();
        } else currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArrayOnDate(dateString);
    }

    public List<String> getBuildPlan() {
        return WebJSONConverter.getCurStringList(currenciesWebJSONArray);
    }

    @Override
    public void reset() {
        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();
    }

    @Override
    public void buildCurrency(String currencyString) {
        JSONObject currencyObject = WebJSONConverter.getCurObjectByCurString(currenciesWebJSONArray,currencyString);
        if(currencyObject != null) {
            BigDecimal ratio = BigDecimal.valueOf(WebJSONConverter.getRatioFromObject(currencyObject));
            long currencyScale = WebJSONConverter.getScaleFromObject(currencyObject);
            try {
                PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO " + tableName +
                        dateString + " (currencyFrom , currencyScale, ratio) VALUES (?, ?, ?)");
                preparedStatement.setString(1, currencyString);
                preparedStatement.setLong(2, currencyScale);
                preparedStatement.setBigDecimal(3, ratio);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Can't find " + currencyString + " currency");
        }
    }
}
