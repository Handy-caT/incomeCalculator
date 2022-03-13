package wallet;

import java.util.HashMap;
import java.util.regex.Pattern;

public class CurrencyUnit {

    private static HashMap<String,Currency> currencyHashMap;
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    private final Currency currency;
    private final String code;

    public CurrencyUnit() {
        putCurrenciesToMap();
        currency = Currency.USD;
        code = "USD";
    }
    public CurrencyUnit(String currencyString) {
        putCurrenciesToMap();
        this.currency = of(currencyString).currency;
        code = currencyString;
    }
    private CurrencyUnit(Currency currency) {
        putCurrenciesToMap();
        this.currency = currency;
        code = currency.toString();
    }

    private static void putCurrenciesToMap() {
        currencyHashMap = new HashMap<>();
        currencyHashMap.put("USD",Currency.USD);
        currencyHashMap.put("BYN",Currency.BYN);
        currencyHashMap.put("EUR",Currency.EUR);
    }

    public static CurrencyUnit of(String currencyString) {
        if(!CURRENCY_PATTERN.matcher(currencyString).matches()) {
            throw new IllegalArgumentException(currencyString + " is not currency string");
        }
        try {
            return new CurrencyUnit(currencyHashMap.get(currencyString));
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't find currency " + currencyString);
        }
    }

    public static boolean equals(CurrencyUnit left, CurrencyUnit right) {
        return left.equals(right);
    }

    public boolean equalTo(CurrencyUnit currency) {
        return this.currency == currency.currency;
    }

    public String toString() {
        return code;
    }

}
