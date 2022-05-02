package wallet.money.currencyUnit.currencyUnitJSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterProvider;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWeb;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider {

    private static CurrencyUpdaterJSON instance;

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private static String jsonPathString;
    private static JSONArray currencyJSONArray;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    private static String dateString;

    private CurrencyUpdaterJSON() throws IOException {
        CurrencyUpdaterJSONBuilder builder = CurrencyUpdaterJSONBuilder.getInstance();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        currencyJSONArray = builder.getResult();

        Date date = new Date();
        dateString = formatter.format(date);
        if(jsonPathString == null) {
            jsonPathString = "json/currencyUpdater" + dateString + ".json";
        } else {
            File file = new File(jsonPathString);
            jsonPathString = changeDateInString(jsonPathString);
            File newFile = new File(jsonPathString);
            file.renameTo(newFile);
        }
        propertiesStorage.addProperty("CurrencyUpdaterPath",jsonPathString);

        File file = new File(jsonPathString);

        FileWriter fileWriter = new FileWriter(file);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    private CurrencyUpdaterJSON(List<String> buildingPlan) throws IOException {
        Date date = new Date();
        dateString = formatter.format(date);

        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUpdater" +dateString + ".json";
        propertiesStorage.addProperty("CurrencyUpdaterPath",jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    private CurrencyUpdaterJSON(String jsonPathString) throws IOException, ParseException {
        CurrencyUpdaterJSON.jsonPathString = jsonPathString;
        Date date = new Date();
        dateString = formatter.format(date);
        if(Objects.equals(dateString,getDateFromName(jsonPathString))) {

            JSONParser jsonParser = new JSONParser();

            FileReader fileReader = new FileReader(jsonPathString);
            currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } else {
           new CurrencyUpdaterJSON();
        }
    }

    private String getDateFromName(String name) {
        String dateString = name.substring(name.length() - 15);
        dateString = dateString.substring(0,10);
        return dateString;
    }
    private String changeDateInString(String name) {
        String start = name.substring(0,name.length()-15);
        Date date = new Date();
        return start + formatter.format(date) + ".json";
    }

    private static CurrencyUpdaterJSON createInstance() throws IOException, ParseException {

        String jsonPathString = (String) propertiesStorage.getProperty("CurrencyUpdaterPath");
        if(jsonPathString == null) {
            return new CurrencyUpdaterJSON();
        } else {
            return new CurrencyUpdaterJSON(jsonPathString);
        }
    }

    public static CurrencyUpdaterJSON getInstance() throws IOException, ParseException {
        if(instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    private JSONObject getJSONObjectByCurrencyString(String currencyName) {
        JSONObject currencyJSONObject;
        JSONObject result = null;

        for( Object currencyObject : currencyJSONArray) {
            currencyJSONObject = (JSONObject) currencyObject;
            String tempCurrencyName = (String) currencyJSONObject.get("currencyName");
            if(Objects.equals(tempCurrencyName, currencyName)) {
                result = currencyJSONObject;
                break;
            }
        }
        return result;
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        CurrencyUpdaterWeb currencyUpdaterWeb = CurrencyUpdaterWeb.getInstance();
        return currencyUpdaterWeb.getRatioOnDate(currencyFrom,currencyTo,date);
    }

    @Override
    public long getCurScale(String currencyName) {
        JSONObject currencyObject = getJSONObjectByCurrencyString(currencyName);
        return (long)currencyObject.get("currencyScale");
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        BigDecimal ratio;
        if(Objects.equals(currencyFrom, currencyTo)) {
            return BigDecimal.ONE;
        } else {
            if(!Objects.equals(currencyFrom, "BYN") && !Objects.equals(currencyTo, "BYN")) {
                JSONObject currencyObject = getJSONObjectByCurrencyString(currencyFrom);
                ratio = BigDecimal.valueOf((double) currencyObject.get("Ratio"));
                JSONObject secondCurrencyObject = getJSONObjectByCurrencyString(currencyTo);
                BigDecimal secondRatio = BigDecimal.valueOf((double)secondCurrencyObject.get("Ratio"));
                ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
            } else if(Objects.equals(currencyFrom, "BYN")) {
                JSONObject currencyObject = getJSONObjectByCurrencyString(currencyTo);
                ratio = BigDecimal.valueOf((double)currencyObject.get("Ratio"));
                ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
            } else {
                JSONObject currencyObject = getJSONObjectByCurrencyString(currencyFrom);
                ratio = BigDecimal.valueOf((double) currencyObject.get("Ratio"));
            }
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
