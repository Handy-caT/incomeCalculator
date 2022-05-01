package wallet.money.currencyUnit;

import wallet.money.currencyUnit.StrictCurrencyUnit;

public interface CurrencyUnitStorage {

    boolean isCurrencyExists(String currencyString);
    StrictCurrencyUnit getCurrencyUnitByCurrencyString(String currencyString);
    StrictCurrencyUnit getCurrencyUnitByCurrencyID(long currencyId);

}