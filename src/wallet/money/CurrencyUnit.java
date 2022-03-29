package wallet.money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

public class CurrencyUnit {

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    private final String currencyName;
    private final BigDecimal currencyId;
    private final BigDecimal currencyScale;

    public CurrencyUnit() {
        currencyName = "USD";
        currencyId = BigDecimal.valueOf(431);
        currencyScale = BigDecimal.ONE;
    }
    public CurrencyUnit(String currencyString) {
        currencyName = currencyString;
        this.currencyScale = BigDecimal.ONE;
        currencyId = null;
    }
    public CurrencyUnit(String currencyString,BigDecimal id) {
        currencyName = currencyString;
        this.currencyId = id;
        this.currencyScale = BigDecimal.ONE;
    }
    public CurrencyUnit(String currencyString,BigDecimal id,BigDecimal scale) {
        currencyName = currencyString;
        this.currencyId = id;
        this.currencyScale = scale;
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
        return Objects.equals(currencyName, that.currencyName) && Objects.equals(currencyId, that.currencyId) && Objects.equals(currencyScale, that.currencyScale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyName, currencyId, currencyScale);
    }

    public boolean equals(String currencyString) {
        return Objects.equals(currencyName, currencyString);
    }

    public String toString() {
        return currencyName;
    }


}
