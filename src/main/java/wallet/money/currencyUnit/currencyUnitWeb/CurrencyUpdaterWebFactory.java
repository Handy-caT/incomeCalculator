package wallet.money.currencyUnit.currencyUnitWeb;

import wallet.money.currencyUnit.interfaces.CurrencyUpdater;
import wallet.money.currencyUnit.interfaces.CurrencyUpdaterFactory;

public class CurrencyUpdaterWebFactory implements CurrencyUpdaterFactory {

    @Override
    public CurrencyUpdater createUpdater() {
        return new CurrencyUpdaterWeb();
    }
}
