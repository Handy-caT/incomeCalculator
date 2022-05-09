package wallet.card.historyKeeper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.PropertiesStorage;
import wallet.card.Card;
import wallet.card.transaction.Transaction;
import wallet.money.CurrencyConverter;
import wallet.money.Money;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class JSONHistoryKeeper extends HistoryKeeper {

    private final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private String jsonPathString;
    private JSONArray snapshotsJSONArray;
    private String dir = "json/";

    public static final String defaultFileName = "cardHistory";
    public static final String propertyName = "cardHistoryPath";

    private static final String beforeBalanceJSONName = "beforeBalance";
    private static final String afterBalanceJSONName = "afterBalance";
    private static final String transactionAmountJSONName = "transactionAmount";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    private static String dateString;

    private String getDateFromName(String name) {
        String dateString = name.substring(name.length() - 15);
        dateString = dateString.substring(0,10);
        return dateString;
    }

    JSONHistoryKeeper() throws IOException {
        Date date = new Date();
        dateString = formatter.format(date);

        jsonPathString = dir + defaultFileName + dateString + ".json";
        propertiesStorage.addProperty(propertyName,jsonPathString);
    }
    JSONHistoryKeeper(String jsonFilePath) throws IOException, ParseException {
        jsonPathString = jsonFilePath;
        Date date = new Date();
        dateString = formatter.format(date);
        if(Objects.equals(dateString,getDateFromName(jsonPathString))) {

            JSONParser jsonParser = new JSONParser();

            FileReader fileReader = new FileReader(jsonPathString);
            snapshotsJSONArray = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } else {
            new JSONHistoryKeeper();
        }
    }

    @Override
    public void restoreTransaction(Card card, Transaction transaction) {
        JSONObject snapshotObject = new JSONObject();
        String moneyString = null;
        for(Object tempObject : snapshotsJSONArray ) {
            snapshotObject = (JSONObject) tempObject;
            moneyString = (String) snapshotObject.get(transactionAmountJSONName);
            if(Objects.equals(moneyString, transaction.toString())) break;
        }
        if(!Objects.equals(moneyString, transaction.toString())) {
            throw new IllegalArgumentException("No transaction found");
        } else {
            Money beforeBalance = Money.parse((String) snapshotObject.get(beforeBalanceJSONName));
            Money afterBalance = Money.parse((String) snapshotObject.get(afterBalanceJSONName));
            restoreTransaction(card, transaction, beforeBalance, afterBalance);
        }
    }

    @Override
    public void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount) {
        JSONObject stateObject = new JSONObject();

        stateObject.put(beforeBalanceJSONName,beforeBalance.toString());
        stateObject.put(transactionAmountJSONName,transactionAmount.toString());
        stateObject.put(afterBalanceJSONName,afterBalance.toString());

        snapshotsJSONArray.add(stateObject);
    }

    @Override
    protected void finalize() throws Throwable {
        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonPathString);
        JSONArray tempArray = (JSONArray) jsonParser.parse(fileReader);
        fileReader.close();
        tempArray.addAll(snapshotsJSONArray);

        FileWriter fileWriter = new FileWriter(jsonPathString);
        tempArray.writeJSONString(fileWriter);
    }

}
