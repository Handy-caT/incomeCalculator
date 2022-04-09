package tests.moneyTests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wallet.card.AddTransaction;
import wallet.card.Card;
import wallet.card.HistoryKeeper;
import wallet.card.JSONHistoryKeeper;
import wallet.money.CurrencyUnitJSONStorage;
import wallet.money.Money;
import wallet.money.StrictCurrencyUnit;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

import static org.junit.Assert.*;

public class CardTest {

    SecureRandom random;

    String jsonFilePath = "testFiles/json/cardHistory.json";

    private BigDecimal randomValue() {
        BigDecimal value = BigDecimal.valueOf(random.nextInt(9999));
        value = value.setScale(2, RoundingMode.DOWN);
        value = value.divide(BigDecimal.valueOf(100));

        return value;
    }

    @Before
    public void setUp() throws Exception {
        random = new SecureRandom();
    }

    @Test
    public void receiveAddTransaction() throws IOException, ParseException {
        HistoryKeeper historyKeeper = new JSONHistoryKeeper(jsonFilePath);
        CurrencyUnitJSONStorage currencyUnitJSONStorage = CurrencyUnitJSONStorage.getInstance();
        StrictCurrencyUnit USDUnit = currencyUnitJSONStorage.getCurrencyUnitByCurrencyString("USD");
        Card card = new Card(historyKeeper,USDUnit);

        Money beforeBalance = card.getBalance();
        Money money = Money.of(USDUnit,randomValue());

        AddTransaction addTransaction = new AddTransaction(card,money);

        card.receiveTransaction(addTransaction);
        historyKeeper.saveState();

        Money balance = card.getBalance();
        Assert.assertEquals(balance.getAmount(),money.getAmount());

        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonFilePath);
        JSONArray array = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();

        JSONObject snapshotObject = (JSONObject) array.get(0);
        Assert.assertEquals(snapshotObject.get("transactionAmount"),money.toString());
        Assert.assertEquals(snapshotObject.get("beforeBalance"),beforeBalance.toString());
        Assert.assertEquals(snapshotObject.get("afterBalance"),balance.toString());

        File file = new File(jsonFilePath);
        file.delete();
    }
}