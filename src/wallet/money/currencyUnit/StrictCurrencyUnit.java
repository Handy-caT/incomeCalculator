package wallet.money.currencyUnit;

import java.math.BigDecimal;
import java.util.Objects;

public class StrictCurrencyUnit {

    protected String currencyName;
    protected long currencyId;
    protected long currencyScale;

    protected StrictCurrencyUnit(){}
    public StrictCurrencyUnit(String currencyString, long id, long scale) {
        currencyName = currencyString;
        this.currencyId = id;
        this.currencyScale = scale;
    }
    public StrictCurrencyUnit(StrictCurrencyUnit currencyUnit) {
        this.currencyId = currencyUnit.currencyId;
        this.currencyScale = currencyUnit.currencyScale;
        this.currencyName = currencyUnit.currencyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrictCurrencyUnit that = (StrictCurrencyUnit) o;
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

    public StrictCurrencyUnit clone() {
        return new StrictCurrencyUnit(this);
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public long getCurrencyScale() {
        return currencyScale;
    }
}
