package wallet.money;

public interface CurrencyUnitStorageBuilder {

    void reset();
    void buildCurrencyUnit(String currencyString);
    static void getInstance() {

    }

}
