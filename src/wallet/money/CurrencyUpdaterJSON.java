package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider{

    private static String JSONFileName = "currenciesToAdd.json";

    @Override
    public BigDecimal getDecimalPlaces(String currencyString) {
        FileReader reader;
        BigDecimal decimalPlaces = BigDecimal.ZERO;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);
            
            String tempString = null;
            int i = 1;
            JSONObject currencyObject = (JSONObject) currenciesJSONArray.get(0);
            tempString = (String) currencyObject.get("currencyName");
            while(!Objects.equals(tempString, currencyString)) {
                currencyObject = (JSONObject) currenciesJSONArray.get(i);
                tempString = (String) currencyObject.get("currencyName");
                i++;
            }
            decimalPlaces = BigDecimal.valueOf((long) currencyObject.get("decimalPlaces"));
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decimalPlaces;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        FileReader reader;
        BigDecimal ratio = null;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);

            String tempString;
            int i = 1;
            JSONObject currencyObject = (JSONObject) currenciesJSONArray.get(0);
            tempString = (String) currencyObject.get("currencyName");
            while(!Objects.equals(tempString, currencyFrom) && i < currenciesJSONArray.size()) {
                currencyObject = (JSONObject) currenciesJSONArray.get(i);
                tempString = (String) currencyObject.get("currencyName");
                i++;
            }
            
            JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
            JSONObject ratioObject = (JSONObject) ratioArray.get(0);
            tempString = (String) ratioObject.get("currencyTo");
            ratio = BigDecimal.valueOf((double) ratioObject.get("ratio"));
            i = 1;
            while(!Objects.equals(tempString, currencyTo) && i < ratioArray.size()) {
                ratioObject = (JSONObject) ratioArray.get(i);
                tempString = (String) ratioObject.get("currencyTo");
                ratio = BigDecimal.valueOf((double) ratioObject.get("ratio"));
                i++;
            }
            if(i > ratioArray.size()) throw new IllegalArgumentException("No such ratio found!");
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratio;
    }

    @Override
    public HashMap<String,BigDecimal> getCurrencyHash(String currencyName) {
        HashMap<String,BigDecimal> currencyHash = new HashMap<>();
        currencyHash.put(currencyName,BigDecimal.ONE);

        FileReader reader;
        BigDecimal ratio = null;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);

            String tempString;
            int i = 1;
            JSONObject currencyObject = (JSONObject) currenciesJSONArray.get(0);
            tempString = (String) currencyObject.get("currencyName");
            while(!Objects.equals(tempString, currencyName)) {
                currencyObject = (JSONObject) currenciesJSONArray.get(i);
                tempString = (String) currencyObject.get("currencyName");
                i++;
            }

            JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
            JSONObject ratioObject;

            i = 0;
            while(i < ratioArray.size()) {
                ratioObject = (JSONObject) ratioArray.get(i);
                tempString = (String) ratioObject.get("currencyTo");
                ratio = BigDecimal.valueOf((double) ratioObject.get("ratio"));
                currencyHash.put(tempString,ratio);
                i++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyHash;
    }

    @Override
    public void addCurrency(String currencyName,BigDecimal decimalPlaces, HashMap<String, BigDecimal> currenciesRatioMap) {

        FileReader reader;
        BigDecimal ratio = null;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);

            JSONObject newCurrencyJSON = new JSONObject();
            JSONArray newCurrencyRatioArray = new JSONArray();
            JSONObject tempRatioObject;

            for(Map.Entry<String,BigDecimal> entry : currenciesRatioMap.entrySet()) {
                tempRatioObject = new JSONObject();
                tempRatioObject.put("currencyTo",entry.getKey());
                tempRatioObject.put("ratio",entry.getValue());

                newCurrencyRatioArray.add(tempRatioObject);
            }

            newCurrencyJSON.put("currencyName",currencyName);
            newCurrencyJSON.put("decimalPlaces",decimalPlaces);
            newCurrencyJSON.put("ratioArray",newCurrencyRatioArray);

            currenciesJSONArray.add(newCurrencyJSON);

            reader.close();

            FileWriter writer = new FileWriter(JSONFileName);
            writer.write(currenciesJSONArray.toString());
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addRatio(String currencyFrom, String currencyTo, BigDecimal ratio) {
        FileReader reader;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);

            String tempString;
            int i = 1;
            JSONObject currencyObject = (JSONObject) currenciesJSONArray.get(0);
            tempString = (String) currencyObject.get("currencyName");
            while (!Objects.equals(tempString, currencyFrom)) {
                currencyObject = (JSONObject) currenciesJSONArray.get(i);
                tempString = (String) currencyObject.get("currencyName");
                i++;
            }

            currenciesJSONArray.remove(i-1);

            JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
            JSONObject tempRatioObject = new JSONObject();
            tempRatioObject.put("currencyTo",currencyTo);
            tempRatioObject.put("ratio",ratio);
            ratioArray.add(tempRatioObject);

            currencyObject.replace("ratioArray",ratioArray);

            currenciesJSONArray.add(currencyObject);

            reader.close();

            FileWriter writer = new FileWriter(JSONFileName);
            writer.write(currenciesJSONArray.toString());
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRatio(String currencyFrom, String currencyTo) {
        FileReader reader;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);

            String tempString;
            int i = 1;
            JSONObject currencyObject = (JSONObject) currenciesJSONArray.get(0);
            tempString = (String) currencyObject.get("currencyName");
            while (!Objects.equals(tempString, currencyFrom)) {
                currencyObject = (JSONObject) currenciesJSONArray.get(i);
                tempString = (String) currencyObject.get("currencyName");
                i++;
            }

            currenciesJSONArray.remove(i-1);

            JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
            JSONObject ratioObject = (JSONObject) ratioArray.get(0);
            tempString = (String) ratioObject.get("currencyTo");
            i = 1;
            while(!Objects.equals(tempString, currencyTo)) {
                ratioObject = (JSONObject) ratioArray.get(i);
                tempString = (String) ratioObject.get("currencyTo");
                i++;
            }
            ratioArray.remove(i-1);

            currencyObject.replace("ratioArray",ratioArray);

            currenciesJSONArray.add(currencyObject);

            reader.close();

            FileWriter writer = new FileWriter(JSONFileName);
            writer.write(currenciesJSONArray.toString());
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCurrency(String currencyName) {

    }

    public static void setJsonFilePath(String filePath) {
        JSONFileName = filePath;
    }

}
