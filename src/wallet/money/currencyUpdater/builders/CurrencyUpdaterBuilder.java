package wallet.money.currencyUpdater.builders;

public interface CurrencyUpdaterBuilder {

    void reset();
    void buildCurrency(String currencyString);

}
