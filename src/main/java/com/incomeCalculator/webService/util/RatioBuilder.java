package com.incomeCalculator.webService.util;

import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterBuilder;
import com.incomeCalculator.core.wallet.money.util.WebApiJSON;
import com.incomeCalculator.core.wallet.money.util.WebJSONConverter;
import com.incomeCalculator.webService.exceptions.CurrencyUnitNotFoundException;
import com.incomeCalculator.webService.models.CurrencyUnitEntity;
import com.incomeCalculator.webService.models.Ratio;
import com.incomeCalculator.webService.repositories.CurrencyUnitRepository;
import com.incomeCalculator.webService.repositories.RatioRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class RatioBuilder implements CurrencyUpdaterBuilder {

    private static final Logger log = LoggerFactory.getLogger(RatioBuilder.class);

    private RatioRepository repository;
    private CurrencyUnitRepository currencyUnitRepository;
    private String dateString;
    private static JSONArray currenciesWebJSONArray;

    public List<String> getBuildPlan() {
        return WebJSONConverter.getCurStringList(currenciesWebJSONArray);
    }

    public RatioBuilder(RatioRepository repository,CurrencyUnitRepository currencyUnitRepository,
                        String dateString) {
        this.dateString = dateString;
        this.repository = repository;
        this.currencyUnitRepository = currencyUnitRepository;

        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        if(webApiJSON.needToUpdate()) webApiJSON.Update();
        currenciesWebJSONArray = webApiJSON.getCurrenciesJSONArray();
    }

    @Override
    public void reset() {
        repository.deleteAllByDateString(dateString);
    }

    @Override
    public void buildCurrency(String currencyString) {
        JSONObject currencyObject = WebJSONConverter.getCurObjectByCurString(currenciesWebJSONArray,currencyString);
        if(currencyObject != null) {
            BigDecimal ratio = BigDecimal.valueOf(WebJSONConverter.getRatioFromObject(currencyObject));

            log.info("Preloading " + repository.save(new Ratio(currencyUnitRepository
                            .findByCurrencyName(currencyString)
                            .orElseThrow(() -> new CurrencyUnitNotFoundException(currencyString)),
                            ratio, dateString)));
        }
    }
}
