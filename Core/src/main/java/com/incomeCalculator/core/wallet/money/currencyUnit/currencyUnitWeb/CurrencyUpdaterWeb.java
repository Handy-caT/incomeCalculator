package com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitWeb;

import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.util.APIProvider;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CurrencyUpdaterWeb implements CurrencyUpdater {

    private static APIProvider api;

    private static String BYNString = "BYN";

    protected CurrencyUpdaterWeb() {}

   public static void setApi(APIProvider api) {
       CurrencyUpdaterWeb.api = api;
   }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo)  {
        BigDecimal ratio;
        if(Objects.equals(currencyFrom,currencyTo)) {
            ratio = BigDecimal.ONE;
        } else {
            JSONArray currenciesArray = api.getRatiosArray();

            ratio = getRatioFromArray(currencyFrom, currencyTo, currenciesArray);
        }
        return ratio;
    }

    private BigDecimal getRatioFromArray(String currencyFrom, String currencyTo, JSONArray currenciesArray) {
        BigDecimal ratio;
        JSONObject fromObject = WebJSONConverter.getCurObjectByCurString(currenciesArray,currencyFrom);
        JSONObject toObject = WebJSONConverter.getCurObjectByCurString(currenciesArray,currencyTo);

        if (!Objects.equals(currencyFrom, BYNString) && !Objects.equals(currencyTo, BYNString)) {
            long scaleFrom = WebJSONConverter.getScaleFromObject(fromObject);
            ratio = BigDecimal.valueOf(WebJSONConverter.getRatioFromObject(fromObject));

            long scaleTo = WebJSONConverter.getScaleFromObject(toObject);
            BigDecimal secondRatio = BigDecimal.valueOf(WebJSONConverter.getRatioFromObject(toObject));

            ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
            ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
            ratio = ratio.multiply(BigDecimal.valueOf(scaleTo));

        } else if(Objects.equals(currencyFrom, BYNString)) {
            long scaleTo = WebJSONConverter.getScaleFromObject(toObject);
            ratio = BigDecimal.valueOf(WebJSONConverter.getRatioFromObject(toObject));
            ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
            ratio.multiply(BigDecimal.valueOf(scaleTo));
        } else {
            long scaleFrom = WebJSONConverter.getScaleFromObject(fromObject);
            ratio = BigDecimal.valueOf(WebJSONConverter.getRatioFromObject(fromObject));
            ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
        }
        return ratio;
    }

    @Override
    public long getCurScale(String currencyName) {
        JSONObject currencyObject = api.getCurrencyUnitObject(currencyName);
        return WebJSONConverter.getScaleFromObject(currencyObject);
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        BigDecimal ratio;
        if(Objects.equals(currencyFrom,currencyTo)) {
            ratio = BigDecimal.ONE;
        } else {
            String dateString = DateFormatter.webFormat(date);
            JSONArray currenciesArray = api.getRatiosArray(dateString);

            ratio = getRatioFromArray(currencyFrom, currencyTo, currenciesArray);
        }
        return ratio;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyToList) {
        Map<String,BigDecimal> currenciesHash = new HashMap<>();
        for(String currencyTo : currencyToList) {
            currenciesHash.put(currencyTo,getRatio(currencyFrom,currencyTo));
        }
        return currenciesHash;
    }

}


