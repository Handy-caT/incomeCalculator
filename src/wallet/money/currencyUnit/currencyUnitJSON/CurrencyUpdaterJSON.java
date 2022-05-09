package wallet.money.currencyUnit.currencyUnitJSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.PropertiesStorage;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWebFactory;
import wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import wallet.money.currencyUnit.currencyUnitWeb.CurrencyUpdaterWeb;
import wallet.money.util.JSONConverter;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyUpdaterJSON implements CurrencyUpdater {

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private static String jsonPathString;
    private static JSONArray currencyJSONArray;
    private static String dir = "json/";

    public static final String defaultFileName = "currencyUpdater";
    public static final String propertyName = "CurrencyUpdaterPath";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    private static String dateString;

    protected CurrencyUpdaterJSON() throws IOException {
        CurrencyUpdaterJSONBuilder builder = CurrencyUpdaterJSONBuilder.getInstance();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        currencyJSONArray = builder.getResult();

        Date date = new Date();
        dateString = formatter.format(date);
        if(jsonPathString == null) {
            jsonPathString = dir+defaultFileName + dateString + ".json";
        } else {
            File file = new File(jsonPathString);
            jsonPathString = changeDateInString(jsonPathString);
            File newFile = new File(jsonPathString);
            file.renameTo(newFile);
        }
        propertiesStorage.addProperty(propertyName,jsonPathString);

        File file = new File(jsonPathString);

        FileWriter fileWriter = new FileWriter(file);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    protected CurrencyUpdaterJSON(List<String> buildingPlan) throws IOException {
        Date date = new Date();
        dateString = formatter.format(date);

        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = dir+defaultFileName +dateString + ".json";
        propertiesStorage.addProperty(propertyName,jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    protected CurrencyUpdaterJSON(String jsonPathString) throws IOException, ParseException {
        this.jsonPathString = jsonPathString;
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

    public void update() throws IOException {
        new CurrencyUpdaterJSON();
    }

    public String getDate() {
        return dateString;
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
        CurrencyUpdaterWebFactory factory = new CurrencyUpdaterWebFactory();
        CurrencyUpdaterWeb currencyUpdaterWeb = (CurrencyUpdaterWeb) factory.createUpdater();
        return currencyUpdaterWeb.getRatioOnDate(currencyFrom,currencyTo,date);
    }

    @Override
    public long getCurScale(String currencyName) {
        JSONObject currencyObject = getJSONObjectByCurrencyString(currencyName);
        return (long)currencyObject.get("currencyScale");
    }

    private BigDecimal getRatioFromArray(String currencyFrom, String currencyTo, JSONArray currenciesArray) {
        BigDecimal ratio;
        JSONObject fromObject = JSONConverter.getCurObjectByCurStringLocal(currenciesArray,currencyFrom);
        JSONObject toObject = JSONConverter.getCurObjectByCurStringLocal(currenciesArray,currencyTo);

        if (!Objects.equals(currencyFrom, "BYN") && !Objects.equals(currencyTo, "BYN")) {
            long scaleFrom = JSONConverter.getScaleFromLocalObject(fromObject);
            ratio = BigDecimal.valueOf(JSONConverter.getRatioFromLocalObject(fromObject));

            long scaleTo = JSONConverter.getScaleFromLocalObject(toObject);
            BigDecimal secondRatio = BigDecimal.valueOf(JSONConverter.getRatioFromLocalObject(toObject));

            ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
            ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
            ratio = ratio.multiply(BigDecimal.valueOf(scaleTo));

        } else if(Objects.equals(currencyFrom, "BYN")) {
            long scaleTo = JSONConverter.getScaleFromLocalObject(toObject);
            ratio = BigDecimal.valueOf(JSONConverter.getRatioFromLocalObject(toObject));
            ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
            ratio.multiply(BigDecimal.valueOf(scaleTo));
        } else {
            long scaleFrom = JSONConverter.getScaleFromLocalObject(fromObject);
            ratio = BigDecimal.valueOf(JSONConverter.getRatioFromLocalObject(fromObject));
            ratio = ratio.divide(BigDecimal.valueOf(scaleFrom));
        }
        return ratio;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        BigDecimal ratio;
        if(Objects.equals(currencyFrom, currencyTo)) {
            return BigDecimal.ONE;
        } else {
            ratio = getRatioFromArray(currencyFrom,currencyTo,currencyJSONArray);
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

    public static void setDir(String dir) {
        CurrencyUpdaterJSON.dir = dir;
    }

}
