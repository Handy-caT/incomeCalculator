package wallet.money;

import java.math.BigDecimal;

public interface CurrencyUnitStorage {

    boolean isCurrencyExists(String currencyString);
    CurrencyUnit getCurrencyUnitByCurrencyString(String currencyString);
    CurrencyUnit getCurrencyUnitByCurrencyID(BigDecimal currencyId);

}
