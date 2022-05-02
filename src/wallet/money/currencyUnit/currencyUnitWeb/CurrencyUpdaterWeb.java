package wallet.money.currencyUnit.currencyUnitWeb;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterProvider;
import wallet.money.util.APIProvider;
import wallet.money.util.JSONConverter;
import wallet.money.util.NBRBAPI;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyUpdaterWeb implements CurrencyUpdaterProvider {

    private static CurrencyUpdaterWeb instance;
    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private static APIProvider api;

    public static CurrencyUpdaterWeb getInstance() {
        if(instance == null) {
            instance = new CurrencyUpdaterWeb();
            if(api == null) api = new NBRBAPI();
        }
        return instance;
    }

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
        JSONObject fromObject = JSONConverter.getCurObjectByCurString(currenciesArray,currencyFrom);
        JSONObject toObject = JSONConverter.getCurObjectByCurString(currenciesArray,currencyTo);

        if (!Objects.equals(currencyFrom, "BYN") && !Objects.equals(currencyTo, "BYN")) {
            long scaleFrom = JSONConverter.getScaleFromObject(fromObject);
            ratio = BigDecimal.valueOf(JSONConverter.getRatioFromObject(fromObject));

            long scaleTo = JSONConverter.getScaleFromObject(toObject);
            BigDecimal secondRatio = BigDecimal.valueOf(JSONConverter.getRatioFromObject(toObject));

            ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
            ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
            ratio = ratio.multiply(BigDecimal.valueOf(scaleTo));

        } else if(Objects.equals(currencyFrom, "BYN")) {
            long scaleTo = JSONConverter.getScaleFromObject(toObject);
            ratio = BigDecimal.valueOf(JSONConverter.getRatioFromObject(toObject));
            ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
            ratio.multiply(BigDecimal.valueOf(scaleTo));
        } else {
            long scaleFrom = JSONConverter.getScaleFromObject(fromObject);
            ratio = BigDecimal.valueOf(JSONConverter.getRatioFromObject(fromObject));
            ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
        }
        return ratio;
    }

    @Override
    public long getCurScale(String currencyName) {
        JSONObject currencyObject = api.getCurrencyUnitObject(currencyName);
        return JSONConverter.getScaleFromObject(currencyObject);
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        BigDecimal ratio;
        if(Objects.equals(currencyFrom,currencyTo)) {
            ratio = BigDecimal.ONE;
        } else {
            String dateString = webFormatter.format(date);
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


