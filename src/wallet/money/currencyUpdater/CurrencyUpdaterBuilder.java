package wallet.money.currencyUpdater;

public interface CurrencyUpdaterBuilder {

    void reset();
    void buildCurrency(String currencyString);

}
