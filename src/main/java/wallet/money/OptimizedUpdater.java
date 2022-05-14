package wallet.money;

import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSON;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSONFactory;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;
import wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQL;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OptimizedUpdater implements CurrencyUpdater, CurrencyUpdaterFactory {

    private final CurrencyUpdaterJSON jsonUpdater;
    private final CurrencyUpdaterSQL sqlUpdater;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");

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
        if(!Objects.equals(jsonUpdater.getDate(), formatter.format(date))){
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
