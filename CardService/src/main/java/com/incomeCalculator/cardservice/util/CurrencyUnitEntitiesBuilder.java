package com.incomeCalculator.cardservice.util;

import com.incomeCalculator.cardservice.repositories.CurrencyUnitRepository;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUnitStorageBuilder;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CurrencyUnitEntitiesBuilder implements CurrencyUnitStorageBuilder {

    private static final Logger log = LoggerFactory.getLogger(CurrencyUnitEntitiesBuilder.class);

    private final CurrencyUnitRepository repository;
    private static JSONArray currenciesWebJSONArray;

    public List<String> getBuildPlan() {
        return WebJSONConverter.getCurStringList(currenciesWebJSONArray);
    }

    public CurrencyUnitEntitiesBuilder(CurrencyUnitRepository repository) {
        this.repository = repository;

        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();
    }

    @Override
    public void buildCurrencyUnit(String currencyString) {

        JSONObject currencyObject = WebJSONConverter.getCurObjectByCurString(currenciesWebJSONArray,currencyString);

        if(currencyObject != null) {
            long currencyId = WebJSONConverter.getIdFromObject(currencyObject);
            long currencyScale = WebJSONConverter.getScaleFromObject(currencyObject);

            log.info("Preloading " + repository
                    .save(new CurrencyUnitEntity(currencyString, currencyId, currencyScale)));
        }
    }

    @Override
    public void reset() {
        repository.deleteAll();
    }
}
