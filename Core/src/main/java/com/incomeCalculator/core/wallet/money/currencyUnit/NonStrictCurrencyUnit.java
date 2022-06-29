package com.incomeCalculator.core.wallet.money.currencyUnit;

import java.util.Objects;
import java.util.regex.Pattern;

public class NonStrictCurrencyUnit implements CurrencyUnit {

    protected String currencyName;

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    protected NonStrictCurrencyUnit(String currencyString) {
        this.currencyName = currencyString;
    }

    public static NonStrictCurrencyUnit of(String currencyString) {
        if(!CURRENCY_PATTERN.matcher(currencyString).matches()) {
            throw new IllegalArgumentException(currencyString + " is not currency string");
        }
        try {
            return new NonStrictCurrencyUnit(currencyString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't find currency " + currencyString);
        }
    }

    public String getCurrencyName() {
        return currencyName;
    }

    @Override
    public long getCurrencyId() {
        return 1;
    }

    @Override
    public long getCurrencyScale() {
        return 1;
    }

    @Override
    public String toString() {
        return currencyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NonStrictCurrencyUnit)) return false;
        NonStrictCurrencyUnit that = (NonStrictCurrencyUnit) o;
        return Objects.equals(currencyName, that.currencyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyName);
    }
}
