package tests.moneyTests;

import org.junit.Test;
import wallet.money.CurrencyUpdaterProvider;
import wallet.money.CurrencyUpdaterWeb;

import static org.junit.Assert.*;

public class CurrencyUpdaterWebTest {

    @Test
    public void getCurrencyHash() {
    }

    @Test
    public void getRatio() {
        CurrencyUpdaterProvider currencyUpdater= new CurrencyUpdaterWeb();
        currencyUpdater.getRatio("USD","BYN");

    }

    @Test
    public void getDecimalPlaces() {
    }
}