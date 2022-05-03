package wallet.money;

import org.json.simple.parser.ParseException;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSON;
import wallet.money.currencyUnit.currencyUnitJSON.CurrencyUpdaterJSONFactory;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQLFactory;
import wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import wallet.money.currencyUnit.currencyUnitSQL.CurrencyUpdaterSQL;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OptimizedUpdater implements CurrencyUpdater {

    private CurrencyUpdaterJSON jsonUpdater;
    private CurrencyUpdaterSQL sqlUpdater;

    private OptimizedUpdater() throws IOException, ParseException, SQLException {
        CurrencyUpdaterJSONFactory jsonFactory = new CurrencyUpdaterJSONFactory();
        CurrencyUpdaterSQLFactory sqlFactory = new CurrencyUpdaterSQLFactory();

        jsonUpdater = (CurrencyUpdaterJSON) jsonFactory.createUpdater();
        sqlUpdater = (CurrencyUpdaterSQL) sqlFactory.createUpdater();
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo) {
        Date date = new Date();
        if(!sqlUpdater.isUpdaterOnDateExist(date)) {
            try {
                sqlUpdater.createUpdaterOnDate(date);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return jsonUpdater.getRatio(currencyFrom,currencyTo);
    }

    @Override
    public BigDecimal getRatioOnDate(String currencyFrom, String currencyTo, Date date) {
        return null;
    }

    @Override
    public long getCurScale(String currencyName) {
        return 0;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRatiosMap(String currencyFrom, List<String> currencyTo) {
        return null;
    }
}
