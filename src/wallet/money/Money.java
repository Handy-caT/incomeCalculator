package wallet.money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    private final CurrencyUnit currency;
    private final BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public Money(CurrencyUnit currency,BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public static Money of(CurrencyUnit currency,BigDecimal amount) {
        return new Money(currency,amount);
    }

    public static Money parse(String moneyString) {
        String formattedMoneyString = moneyString.replaceAll(" ","");
        if(formattedMoneyString.length() < 4){
            throw new IllegalArgumentException(moneyString.trim() + " can't be parsed");
        }
        String currencyString = formattedMoneyString.substring(0,3);
        String amountString = formattedMoneyString.substring(3);
        return of(CurrencyUnit.of(currencyString),new BigDecimal(amountString));
    }

    public CurrencyUnit getCurrency() {
        return currency;
    }

    public boolean isSameCurrency(Money money) {
        return (currency.equals(money.currency));
    }

    public Money plus(Money money) {
        BigDecimal newAmount;
        if(isSameCurrency(money)) {
            newAmount = this.amount.add(money.amount);
        } else {
            Money convertedMoney = CurrencyConverter.convert(money,currency);
            newAmount = this.amount.add(convertedMoney.amount);
        }
        return new Money(this.currency,newAmount);
    }
    public Money minus(Money money) {
        BigDecimal newAmount;
        if(isSameCurrency(money)) {
            newAmount = this.amount.subtract(money.amount);
        } else {
            Money convertedMoney = CurrencyConverter.convert(money,currency);
            newAmount = this.amount.subtract(convertedMoney.amount);
        }
        return new Money(this.currency,newAmount);
    }

    public Money plus(BigDecimal moneyAmount) {
        BigDecimal newAmount = this.amount.add(moneyAmount);
        return new Money(this.currency,newAmount);
    }
    public Money minus(BigDecimal moneyAmount) {
        BigDecimal newAmount = this.amount.subtract(moneyAmount);
        return new Money(this.currency,newAmount);
    }
    public Money multiply(BigDecimal value) {
        BigDecimal newAmount = this.amount.multiply(value);
        return new Money(this.currency,newAmount);
    }
    public Money divide(BigDecimal value) {
        BigDecimal newAmount = this.amount.divide(value, RoundingMode.DOWN);
        return new Money(this.currency,newAmount);
    }
    public Money divide(BigDecimal value, RoundingMode roundingMode) {
        BigDecimal newAmount = this.amount.divide(value, roundingMode);
        return new Money(this.currency,newAmount);
    }

    public String toString() {
        return new StringBuilder().append(currency.toString()).append(" ").append(amount.toString()).toString();
    }

}

