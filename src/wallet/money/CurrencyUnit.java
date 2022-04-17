package wallet.money;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class CurrencyUnit extends StrictCurrencyUnit {

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    protected CurrencyUnit(String currencyString) {
        super();
        this.currencyName = currencyString;
        this.currencyId = 0;
        this.currencyScale = BigDecimal.ONE;
    }
    public CurrencyUnit(StrictCurrencyUnit currencyUnit) {
        super(currencyUnit);
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


}
