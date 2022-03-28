package wallet.money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class CurrencyUnit {

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    private final String code;
    private final BigDecimal id;
    private final BigDecimal scale;

    public CurrencyUnit() {
        code = "USD";
        id = BigDecimal.valueOf(431);
        scale = BigDecimal.ONE;
    }
    public CurrencyUnit(String currencyString) {
        code = currencyString;
        this.scale = BigDecimal.ONE;
        id = null;
    }
    public CurrencyUnit(String currencyString,BigDecimal id) {
        code = currencyString;
        this.id = id;
        this.scale = BigDecimal.ONE;
    }
    public CurrencyUnit(String currencyString,BigDecimal id,BigDecimal scale) {
        code = currencyString;
        this.id = id;
        this.scale = scale;
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
        return Objects.equals(code, that.code) && Objects.equals(id, that.id) && Objects.equals(scale, that.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, id, scale);
    }

    public boolean equals(String currencyString) {
        return Objects.equals(code, currencyString);
    }

    public String toString() {
        return code;
    }


}
