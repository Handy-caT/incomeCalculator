package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider{

    private String JSONFileName;

    CurrencyUpdaterJSON(String JSONFileName) {
        this.JSONFileName = JSONFileName;
    }

    private JSONObject getCurrencyJSONObject(String currencyString) {
        FileReader reader;
        JSONObject currencyObject = null;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);

            String tempString;
            int i = 1;
            currencyObject = (JSONObject) currenciesJSONArray.get(0);
            tempString = (String) currencyObject.get("currencyName");
            while (!Objects.equals(tempString, currencyString) && i < currenciesJSONArray.size()) {
                currencyObject = (JSONObject) currenciesJSONArray.get(i);
                tempString = (String) currencyObject.get("currencyName");
                i++;
            }

            if(i == currenciesJSONArray.size() && !Objects.equals(tempString, currencyString)) {
                throw new IllegalArgumentException("No currency " + currencyString + " found");
            }

            reader.close();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencyObject;
    }

    @Override
    public BigDecimal getCurScale(String currencyName) {
        return null;
    }

    @Override
    public BigDecimal getCurID(String currencyName) {
        return null;
    }

    public BigDecimal getDecimalPlaces(String currencyString) {
        JSONObject currencyObject = getCurrencyJSONObject(currencyString);
        BigDecimal decimalPlaces = BigDecimal.valueOf((long)currencyObject.get("decimalPlaces"));
        return decimalPlaces;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {

        BigDecimal ratio;

        JSONObject currencyObject = getCurrencyJSONObject(currencyFrom);

        JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
        JSONObject ratioObject = (JSONObject) ratioArray.get(0);
        String tempString = (String) ratioObject.get("currencyTo");
        ratio = BigDecimal.valueOf((double) ratioObject.get("ratio"));
        int i = 1;
        while (!Objects.equals(tempString, currencyTo) && i < ratioArray.size()) {
            ratioObject = (JSONObject) ratioArray.get(i);
            tempString = (String) ratioObject.get("currencyTo");
            ratio = BigDecimal.valueOf((double) ratioObject.get("ratio"));
            i++;
        }
        if (!Objects.equals(tempString, currencyTo)) return null;


        return ratio;
    }

    @Override
    public HashMap<String,BigDecimal> getCurrencyHash(String currencyString) {
        HashMap<String, BigDecimal> currencyHash = new HashMap<>();
        currencyHash.put(currencyString, BigDecimal.ONE);

        String tempString;
        BigDecimal ratio;

        JSONObject currencyObject = getCurrencyJSONObject(currencyString);
        JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
        JSONObject ratioObject;

        int i = 0;
        while (i < ratioArray.size()) {
            ratioObject = (JSONObject) ratioArray.get(i);
            tempString = (String) ratioObject.get("currencyTo");
            ratio = BigDecimal.valueOf((double) ratioObject.get("ratio"));
            currencyHash.put(tempString, ratio);
            i++;
        }

        return currencyHash;
    }

    public void setJsonFilePath(String filePath) {
        JSONFileName = filePath;
    }

}
