package wallet.money;

import java.math.BigDecimal;

public interface CurrencyUnitStorage {

    boolean isCurrencyExists(String currencyString);
    StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString);
    StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId);

}
