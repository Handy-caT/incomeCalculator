package wallet.money.currencyUnit.currencyUnitJSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wallet.money.util.JSONConverter;
import wallet.money.util.WebApiJSON;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorageBuilder;

import java.util.List;


public class CurrencyUnitJSONStorageBuilder implements CurrencyUnitStorageBuilder {

    private static CurrencyUnitJSONStorageBuilder instance;

    private static JSONArray currenciesWebJSONArray;
    private static JSONArray currenciesJSONArray;

    private CurrencyUnitJSONStorageBuilder() {
        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();

        currenciesJSONArray = new JSONArray();
    }

    public static CurrencyUnitJSONStorageBuilder getInstance() {
        if(instance == null) {
            instance = new CurrencyUnitJSONStorageBuilder();
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
    public void buildCurrencyUnit(String currencyString) {
        JSONObject currencyObject = JSONConverter.getCurObjectByCurString(currenciesWebJSONArray,currencyString);
        JSONObject localCurrencyObject = JSONConverter.convertWebCurObjectToLocal(currencyObject);

        currenciesJSONArray.add(localCurrencyObject);
    }

    public JSONArray getResult() {
        return currenciesJSONArray;
    }

}
