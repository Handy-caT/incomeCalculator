package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider {

    private static CurrencyUpdaterJSON instance;

    public static String propertiesString = "properties/config.properties";
    private static String jsonPathString;
    private static JSONArray currencyJSONArray;

    private CurrencyUpdaterJSON() throws IOException {
        CurrencyUpdaterJSONBuilder builder = CurrencyUpdaterJSONBuilder.getInstance();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUpdater.json";
        addJsonPathToProperties(jsonPathString);

        File file = new File(jsonPathString);
        File dir = new File("json/");
        dir.mkdir();
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    private CurrencyUpdaterJSON(List<String> buildingPlan) throws IOException {
        CurrencyUnitJSONStorageBuilder builder = CurrencyUnitJSONStorageBuilder.getInstance();
        builder.reset();
        for (String currencyString : buildingPlan) {
            builder.buildCurrencyUnit(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUpdater.json";
        addJsonPathToProperties(jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        currencyJSONArray.writeJSONString(fileWriter);
        fileWriter.close();
    }
    private CurrencyUpdaterJSON(String jsonPathString) throws IOException, ParseException {
        this.jsonPathString = jsonPathString;

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonPathString);
        currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();
    }

    private static CurrencyUpdaterJSON createInstance() throws IOException, ParseException {
        FileInputStream fis = new FileInputStream(propertiesString);
        Properties properties = new Properties();
        properties.load(fis);

        String jsonPathString = (String) properties.get("CurrencyUpdaterPath");
        if(jsonPathString == null) {
            return new CurrencyUpdaterJSON();
        } else {
            return new CurrencyUpdaterJSON(jsonPathString);
        }
    }
    private static void addJsonPathToProperties(String jsonPathString) throws IOException {
        FileInputStream fis = new FileInputStream(propertiesString);
        Properties properties = new Properties();
        properties.load(fis);

        properties.put("CurrencyUpdaterPath",jsonPathString);
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
    public BigDecimal getCurScale(String currencyName) {
        JSONObject currencyObject = getJSONObjectByCurrencyString(currencyName);
        return BigDecimal.valueOf((long)currencyObject.get("currencyScale"));
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        BigDecimal ratio;
        if(Objects.equals(currencyFrom, currencyTo)) {
            return BigDecimal.ONE;
        } else {
            if(!Objects.equals(currencyFrom, "BYN") && !Objects.equals(currencyTo, "BYN")) {
                JSONObject currencyObject = getJSONObjectByCurrencyString(currencyFrom);
                ratio = (BigDecimal) currencyObject.get("Ratio");
                JSONObject secondCurrencyObject = getJSONObjectByCurrencyString(currencyTo);
                BigDecimal secondRatio = (BigDecimal)secondCurrencyObject.get("Ratio");
                ratio = ratio.divide(secondRatio, RoundingMode.DOWN);
            } else if(Objects.equals(currencyFrom, "BYN")) {
                JSONObject currencyObject = getJSONObjectByCurrencyString(currencyTo);
                ratio = (BigDecimal) currencyObject.get("Ratio");
            } else {
                JSONObject currencyObject = getJSONObjectByCurrencyString(currencyFrom);
                ratio = (BigDecimal) currencyObject.get("Ratio");
                ratio = BigDecimal.ONE.setScale(4).divide(ratio,RoundingMode.DOWN);
            }
        }
        return ratio;
    }

    @Override
    public BigDecimal getCurID(String currencyName) {
        JSONObject currencyObject = getJSONObjectByCurrencyString(currencyName);
        return BigDecimal.valueOf((long)currencyObject.get("currencyId"));
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
