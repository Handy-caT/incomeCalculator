package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider{

    private static String JSONFileName = "currencies.json";

    private static HashMap<String,HashMap<String, BigDecimal>> converterMapSell;
    static {
        converterMapSell = new HashMap<>();
        FileReader reader;
        try {
            reader = new FileReader(JSONFileName);
            JSONParser jsonParser = new JSONParser();
            JSONArray currenciesJSONArray = (JSONArray) jsonParser.parse(reader);

            JSONObject currencyObject = (JSONObject) currenciesJSONArray.get(0);

            HashMap<String,BigDecimal> Converter = new HashMap<>();
            String currencyName = (String) currencyObject.get("currencyName");
            Converter.put(currencyName,BigDecimal.valueOf(1));

            JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
            int i = 0;
            while(i < ratioArray.size()) {
                JSONObject ratioObject = (JSONObject) ratioArray.get(i);
                String currencyTo = (String) ratioObject.get("currencyTo");
                BigDecimal ratio = (BigDecimal) ratioObject.get("ratio");
                Converter.put(currencyTo,ratio);
                i++;
            }

            converterMapSell.put(currencyName,Converter);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public short getDecimalPlaces(String currencyString) {
        FileReader reader;
        short decimalPlaces = 0;
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
            decimalPlaces = (short) currencyObject.get("decimalPlaces");
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

            String tempString = null;
            int i = 1;
            JSONObject currencyObject = (JSONObject) currenciesJSONArray.get(0);
            tempString = (String) currencyObject.get("currencyName");
            while(!Objects.equals(tempString, currencyFrom)) {
                currencyObject = (JSONObject) currenciesJSONArray.get(i);
                tempString = (String) currencyObject.get("currencyName");
                i++;
            }
            
            JSONArray ratioArray = (JSONArray) currencyObject.get("ratioArray");
            JSONObject ratioObject = (JSONObject) ratioArray.get(0);
            tempString = (String) currencyObject.get("currencyTo");
            i = 1;
            while(!Objects.equals(tempString, currencyTo)) {
                ratioObject = (JSONObject) ratioArray.get(i);
                tempString = (String) ratioObject.get("currencyTo");
                ratio = (BigDecimal) ratioObject.get("ratio");
                i++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratio;
    }

    @Override
    public void addCurrency(String currencyName, HashMap<String, BigDecimal> currenciesRatioMap) {

    }

    @Override
    public void saveCurrenciesState() {

    }
}
