package wallet.money;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class CurrencyUnit {

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    private final String code;
    private final short decimalPlaces;

    public CurrencyUnit() {
        code = "USD";
        decimalPlaces = 2;
    }
    public CurrencyUnit(String currencyString) {
        code = currencyString;
        this.decimalPlaces = 2;
    }
    public CurrencyUnit(String currencyString, short decimalPlaces) {
        code = currencyString;
        this.decimalPlaces = decimalPlaces;
    }


    public static CurrencyUnit of(String currencyString) {
        if(!CURRENCY_PATTERN.matcher(currencyString).matches()) {
            throw new IllegalArgumentException(currencyString + " is not currency string");
        }
        try {
            return new CurrencyUnit(currencyString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't find currency " + currencyString);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyUnit that = (CurrencyUnit) o;
        return decimalPlaces == that.decimalPlaces && Objects.equals(code, that.code);
    }

    public boolean equals(String currencyString) {
        return Objects.equals(code, currencyString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, decimalPlaces);
    }

    public String toString() {
        return code;
    }


}
