package com.incomeCalculator.core.wallet.money;

import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQL;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import com.incomeCalculator.core.wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;
import com.incomeCalculator.core.wallet.money.util.DateFormatter;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSON;
import com.incomeCalculator.core.wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSONFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OptimizedUpdater implements CurrencyUpdater, CurrencyUpdaterFactory {

    private final CurrencyUpdaterJSON jsonUpdater;
    private final CurrencyUpdaterSQL sqlUpdater;

    public OptimizedUpdater() {
        CurrencyUpdaterJSONFactory jsonFactory = new CurrencyUpdaterJSONFactory();
        CurrencyUpdaterSQLFactory sqlFactory = new CurrencyUpdaterSQLFactory();

        jsonUpdater = (CurrencyUpdaterJSON) jsonFactory.createUpdater();
        sqlUpdater = (CurrencyUpdaterSQL) sqlFactory.createUpdater();
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        checkForUpdates();
        return jsonUpdater.getRatio(currencyFrom,currencyTo);
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        return sqlUpdater.getRatioOnDate(currencyFrom,currencyTo,date);
    }

    @Override
    public long getCurScale(String currencyName) {
        return jsonUpdater.getCurScale(currencyName);
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo) {
        checkForUpdates();
        return jsonUpdater.getCurrencyRatiosMap(currencyFrom,currencyTo);
    }

    private void checkForUpdates() {
        Date date = new Date();
        if(!Objects.equals(jsonUpdater.getDate(), DateFormatter.sqlFormat(date))){
            try {
                jsonUpdater.update();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!sqlUpdater.isUpdaterOnDateExist(date)) {
            try {
                sqlUpdater.createUpdaterOnDate(date);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CurrencyUpdater createUpdater() {
        return this;
    }
}
