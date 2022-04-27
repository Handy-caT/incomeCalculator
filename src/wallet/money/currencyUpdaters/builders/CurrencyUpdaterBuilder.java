package wallet.money.currencyUpdaters.builders;

public interface CurrencyUpdaterBuilder {

    void reset();
    void buildCurrency(String currencyString);

}
