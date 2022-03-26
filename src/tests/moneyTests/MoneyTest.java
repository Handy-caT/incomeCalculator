package tests.moneyTests;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wallet.money.CurrencyUnit;
import wallet.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class MoneyTest {

    SecureRandom random;

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value.divide(BigDecimal.valueOf(100));

        return value;
    }

    @Before
    public void beforeMethod() {
        random = new SecureRandom();
    }

    @Test
    public void parseTest() {
        BigDecimal value = randomValue();
        String currencyString = "USD";
        String parseString = value + " " + currencyString;
        Money result = Money.parse(parseString);

        Assert.assertEquals(value,result.getAmount());
        Assert.assertEquals(CurrencyUnit.of(currencyString),result.getCurrency());
    }

}