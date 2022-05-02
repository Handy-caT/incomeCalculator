package wallet.money.currencyUnit.currencyUnitWeb;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import wallet.money.util.APIProvider;
import wallet.money.util.JSONConverter;
import wallet.money.util.NBRBAPI;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import wallet.money.currencyUnit.StrictCurrencyUnit;

public class CurrencyUnitWebStorage implements CurrencyUnitStorage {

    private static CurrencyUnitWebStorage instance;
    private APIProvider api;

    public static CurrencyUnitWebStorage getInstance() {
        if(instance == null) {
            instance = new CurrencyUnitWebStorage();
            instance.api = new NBRBAPI();
        }
        return instance;
    }

    public static CurrencyUnitWebStorage getInstance(APIProvider api) {
        if(instance == null) {
            instance = new CurrencyUnitWebStorage();
            instance.api = api;
        }
        return instance;
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId)  {

        JSONObject currencyObject = api.getCurrencyUnitObject(currencyId);

        String currencyString = JSONConverter.getNameFromObject(currencyObject);
        long scale = JSONConverter.getScaleFromObject(currencyObject);

        return new StrictCurrencyUnit(currencyString,currencyId,scale);
    }

    @Override
    public StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString) {

        JSONObject currencyObject = api.getCurrencyUnitObject(currencyString);

        long currencyId = JSONConverter.getIdFromObject(currencyObject);
        long scale = JSONConverter.getScaleFromObject(currencyObject);

        return new StrictCurrencyUnit(currencyString,currencyId,scale);
    }

    @Override
    public boolean isCurrencyExists(String currencyString) {
        String url = "https://www.nbrb.by/api/exrates/rates/";
        String params = "?parammode=2";
        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(url + currencyString + params).asString();
        } catch (UnirestException e) {
            return false;
        }
        return true;
    }

}
