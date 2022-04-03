package wallet.money;

import org.json.simple.JSONArray;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CurrencyUpdaterJSON implements CurrencyUpdaterProvider {

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
    }

    private static void addJsonPathToProperties(String jsonPathString) throws IOException {
        FileInputStream fis = new FileInputStream("properties/json.properties");
        Properties properties = new Properties();
        properties.load(fis);

        properties.put("CurrencyUpdaterPath",jsonPathString);
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
