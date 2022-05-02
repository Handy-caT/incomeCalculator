package wallet.money.currencyUnit.currencyUnitWeb;

import wallet.PropertiesStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorageFactory;

public class CurrencyUnitWebStorageFactory implements CurrencyUnitStorageFactory {

    @Override
    public CurrencyUnitStorage createStorage() {
        return new CurrencyUnitWebStorage();
    }
}
