package wallet.money;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider {

    private static CurrencyUpdaterJSON instance;

    private String jsonPathString;
    private static JSONArray currencyJSONArray;

    private CurrencyUpdaterJSON() throws IOException {
        CurrencyUpdaterJSONBuilder builder = CurrencyUpdaterJSONBuilder.getInstance();
        builder.reset();
        List<String> buildingPlan = builder.getBuildPlan();
        for (String currencyString : buildingPlan) {
            builder.buildCurrency(currencyString);
        }
        currencyJSONArray = builder.getResult();

        jsonPathString = "json/currencyUpdater.json";
        addJsonPathToProperties(jsonPathString);

        FileWriter fileWriter = new FileWriter(jsonPathString);
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
        FileInputStream fis = new FileInputStream("properties/json.properties");
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
        FileInputStream fis = new FileInputStream("properties/json.properties");
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


    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        return null;
    }

    @Override
    public BigDecimal getCurScale(String currencyName) {
        return null;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        return null;
    }

    @Override
    public BigDecimal getCurID(String currencyName) {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo) {
        return null;
    }
}
