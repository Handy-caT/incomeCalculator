package wallet.money;

public interface CurrencyUpdaterBuilder {

    void reset();
    void buildCurrency(String currencyString);

}
