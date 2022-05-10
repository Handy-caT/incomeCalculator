package wallet.money.currencyUnit.currencyUnitJSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wallet.money.util.JSONConverter;
import wallet.money.util.WebApiJSON;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterBuilder;

import java.util.List;

public class CurrencyUpdaterJSONBuilder implements CurrencyUpdaterBuilder {

    private static CurrencyUpdaterJSONBuilder instance;

    private static JSONArray currenciesWebJSONArray;
    private static JSONArray currenciesJSONArray;

    private CurrencyUpdaterJSONBuilder() {
        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();

        currenciesJSONArray = new JSONArray();
    }

    public static CurrencyUpdaterJSONBuilder getInstance() {
        if(instance == null) {
            instance = new CurrencyUpdaterJSONBuilder();
        }
        return instance;
    }

    public List<String> getBuildPlan() {
        return JSONConverter.getCurStringList(currenciesWebJSONArray);
    }

    @Override
    public void reset() {
        currenciesJSONArray = new JSONArray();
    }
    @Override
    public void buildCurrency(String currencyString) {
        JSONObject currencyObject = JSONConverter.getCurObjectByCurString(currenciesWebJSONArray,currencyString);
        JSONObject localCurrencyObject = JSONConverter.convertWebCurObjectToLocal(currencyObject);

        currenciesJSONArray.add(localCurrencyObject);
    }

    public JSONArray getResult() {
        return currenciesJSONArray;
    }

}
