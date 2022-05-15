package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterBuilder;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CurrencyUpdaterSQLBuilder implements CurrencyUpdaterBuilder {

    private final String tableName;
    private final Connection dbConnection;
    private final String dateString;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");

    private  JSONArray currenciesWebJSONArray;

    public CurrencyUpdaterSQLBuilder(String tableName, Connection dbConnection, String dateString) {

        this.dateString = dateString;
        this.dbConnection = dbConnection;
        this.tableName = tableName;

        Date date = new Date();
        String currentDateString = formatter.format(date);

        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(currentDateString.equals(dateString)) {
            currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();
        } else currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArrayOnDate(dateString);
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
            String tempCurrencyString = WebJSONConverter.getNameFromObject(currencyObject);
            if(Objects.equals(tempCurrencyString, currencyString)) break;
        }
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
