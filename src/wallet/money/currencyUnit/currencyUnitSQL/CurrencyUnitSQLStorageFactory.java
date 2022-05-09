package wallet.money.currencyUnit.currencyUnitSQL;

import wallet.PropertiesStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorage;
import wallet.money.currencyUnit.interfaces.CurrencyUnitStorageFactory;

public class CurrencyUnitSQLStorageFactory implements CurrencyUnitStorageFactory {

    public static final String propertyName = "CurrencyUnitSQLTableName";
    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();

    @Override
    public CurrencyUnitStorage createStorage() {
        String tableName = (String) propertiesStorage.getProperty(propertyName);
        try {
            if (tableName == null) {
                return new CurrencyUnitSQLStorage();
            } else {
                return new CurrencyUnitSQLStorage(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
