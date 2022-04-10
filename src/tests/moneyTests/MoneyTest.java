package tests.moneyTests;


import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wallet.money.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class MoneyTest {

    SecureRandom random;

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

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
        String parseString = currencyString + " " + value;
        Money result = Money.parse(parseString);

        Assert.assertEquals(value,result.getAmount());
        Assert.assertEquals(CurrencyUnit.of(currencyString),result.getCurrency());
    }

    @Test
    public void parseNegativeTest() {
        BigDecimal value = randomValue();
        String currencyString = "USD";
        String parseString = currencyString + " " + value.negate();
        Money result = Money.parse(parseString);

        Assert.assertEquals(value.negate(),result.getAmount());
        Assert.assertEquals(CurrencyUnit.of(currencyString),result.getCurrency());
    }

    @Test
    public void plusTest() {
        BigDecimal valueFirst = randomValue();
        BigDecimal valueSecond = randomValue();
        String currencyString = "USD";

        Money moneyFirst = Money.of(currencyString,valueFirst);
        Money moneySecond = Money.of(currencyString,valueSecond);

        Money result = moneyFirst.plus(moneySecond);

        Assert.assertEquals(valueFirst.add(valueSecond),result.getAmount());
        Assert.assertEquals(CurrencyUnit.of(currencyString),result.getCurrency());
    }

    @Test
    public void multiplyTest() {
        BigDecimal valueFirst = randomValue();
        BigDecimal valueSecond = randomValue();
        String currencyString = "USD";

        Money moneyFirst = Money.of(currencyString,valueFirst);

        Money result = moneyFirst.multiply(valueSecond);

        Assert.assertEquals(valueFirst.multiply(valueSecond),result.getAmount());
        Assert.assertEquals(CurrencyUnit.of(currencyString),result.getCurrency());
    }

    @Test
    public void minusTest() {
        BigDecimal valueFirst = randomValue();
        BigDecimal valueSecond = randomValue();
        String currencyString = "USD";

        Money moneyFirst;
        Money moneySecond;

        if(valueFirst.compareTo(valueSecond) > 0) {
            moneyFirst = Money.of(currencyString,valueFirst);
            moneySecond = Money.of(currencyString,valueSecond);
        } else {
            moneyFirst = Money.of(currencyString,valueSecond);
            moneySecond = Money.of(currencyString,valueFirst);
        }

        Money result = moneyFirst.minus(moneySecond);

        Assert.assertEquals(valueFirst.subtract(valueSecond).abs(),result.getAmount());
        Assert.assertEquals(CurrencyUnit.of(currencyString),result.getCurrency());
    }

    @Test
    public void convertTest() throws IOException, ParseException {
        CurrencyConverter currencyConverter = CurrencyConverter.getInstance();

        BigDecimal valueFirst = randomValue();
        BigDecimal valueSecond = randomValue();
        String currencyFirstString = "USD";
        String currencySecondString = "BYN";

        Money moneyFirst = Money.of(currencyFirstString,valueFirst);
        Money moneySecond = Money.of(currencySecondString,valueSecond);

        BigDecimal ratio = currencyConverter
                .getConvertSellRatio(CurrencyUnit.of(currencyFirstString),CurrencyUnit.of(currencySecondString));
        try {
            moneyFirst.plus(moneySecond);
            Assert.fail("Mustn't plus different currencies");
        } catch (IllegalArgumentException e) {
            Money recountedMoney = currencyConverter.convert(moneySecond,CurrencyUnit.of(currencyFirstString));
            Money result = moneyFirst.plus(recountedMoney);

            Assert.assertEquals(valueSecond.multiply(ratio),recountedMoney.getAmount());
            Assert.assertEquals(valueSecond.multiply(ratio).add(valueFirst),result.getAmount());
            Assert.assertEquals(CurrencyUnit.of(currencyFirstString),result.getCurrency());
        }

    }

}