package wallet.money;

import java.math.BigDecimal;
import java.util.List;

public class CurrencyUnitJSONStorage implements CurrencyUnitStorage {

    private String jsonPathString;

    public CurrencyUnitJSONStorage() {
        CurrencyUnitJSONStorageBuilder builder = new CurrencyUnitJSONStorageBuilder();
        List<String> buildingPlan = builder.getBuildPlan();

    }

    public CurrencyUnitJSONStorage(String jsonPathString) {
        this.jsonPathString = jsonPathString;
    }

    @Override
    public CurrencyUnit getCurrencyUnitByCurrencyString(String currencyString) {
        return null;
    }

    @Override
    public CurrencyUnit getCurrencyUnitByCurrencyID(BigDecimal currencyId) {
        return null;
    }

    @Override
    public boolean isCurrencyExists(String currencyString) {
        return false;
    }
}
