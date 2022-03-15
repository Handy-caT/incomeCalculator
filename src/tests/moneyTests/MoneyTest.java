package tests.moneyTests;

import org.junit.Before;
import org.junit.Test;
import wallet.money.CurrencyUnit;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class MoneyTest {

    Money testMoney;

    @Before
    public void beforeSetUp() {
        testMoney = new Money(CurrencyUnit.of("USD"),new BigDecimal("30"));
    }

    @Test
    public void moneyParseTest() {
        Money anotherTestMoney = Money.parse("USD 23.75");
        assertEquals(anotherTestMoney.getAmount(), new BigDecimal("23.75"));
        assert anotherTestMoney.getCurrency().equals("USD");
    }

    @Test
    public void isEqualCurrencyTest() {
        Money anotherTestMoney = new Money(CurrencyUnit.of("USD"),new BigDecimal("23.75"));
        assert anotherTestMoney.isSameCurrency(testMoney);
    }

    @Test
    public void moneySameCurrencyAdd() {
        Money anotherTestMoney = new Money(CurrencyUnit.of("USD"),new BigDecimal("23.75"));
        anotherTestMoney = testMoney.plus(anotherTestMoney);
        assertEquals(anotherTestMoney.getAmount(), new BigDecimal("53.75"));
        assert anotherTestMoney.getCurrency().equals("USD");
    }

    @Test
    public void moneyOtherCurrencyAdd() {
        Money anotherTestMoney = new Money(CurrencyUnit.of("EUR"),new BigDecimal("10"));
        Money result = testMoney.plus(anotherTestMoney);
        BigDecimal resultAmount = testMoney.getAmount().add(new BigDecimal("10")
                .multiply(CurrencyConverter.getConvertSellRatio(anotherTestMoney.getCurrency(),testMoney.getCurrency())));
        assertEquals(result.getAmount(), resultAmount);
        assert result.getCurrency().equals("USD");
    }

    @Test
    public void moneySameCurrencySubtract() {
        Money anotherTestMoney = new Money(CurrencyUnit.of("USD"),new BigDecimal("23.75"));
        anotherTestMoney = testMoney.minus(anotherTestMoney);
        assertEquals(anotherTestMoney.getAmount(), new BigDecimal("6.25"));
        assert anotherTestMoney.getCurrency().equals("USD");
    }

    @Test
    public void moneyOtherCurrencySubtract() {
        Money anotherTestMoney = new Money(CurrencyUnit.of("EUR"),new BigDecimal("10"));
        Money result = testMoney.minus(anotherTestMoney);
        BigDecimal resultAmount = testMoney.getAmount().subtract(new BigDecimal("10")
                .multiply(CurrencyConverter.getConvertSellRatio(anotherTestMoney.getCurrency(),testMoney.getCurrency())));
        assertEquals(result.getAmount(), resultAmount);
        assert result.getCurrency().equals("USD");
    }

    @Test
    public void moneyMultiply() {
        Money result = testMoney.multiply(BigDecimal.valueOf(1.5));
        BigDecimal resultAmount = testMoney.getAmount()
                .multiply(BigDecimal.valueOf(1.5));
        assertEquals(result.getAmount(), resultAmount);
        assert result.getCurrency().equals("USD");
    }

    @Test
    public void moneyDivide() {
        Money result = testMoney.divide(BigDecimal.valueOf(1.5));
        BigDecimal resultAmount = testMoney.getAmount()
                .divide(BigDecimal.valueOf(1.5), RoundingMode.DOWN);
        assertEquals(result.getAmount(), resultAmount);
        assert result.getCurrency().equals("USD");
    }

}