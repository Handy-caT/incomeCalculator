package wallet.card.historyKeeper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wallet.card.Card;
import wallet.card.historyKeeper.HistoryKeeper;
import wallet.card.transaction.AddTransaction;
import wallet.card.transaction.ReduceTransaction;
import wallet.card.transaction.Transaction;
import wallet.money.Money;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Objects;

public class JSONHistoryKeeper implements HistoryKeeper {

    private JSONArray snapshotsArray;
    private String jsonArrayString;

    public JSONHistoryKeeper() {
        snapshotsArray = new JSONArray();
        jsonArrayString = "json/CardHistory.json";
    }
    public JSONHistoryKeeper(String jsonArrayString) {
        this.jsonArrayString = jsonArrayString;
        try {
            JSONParser jsonParser = new JSONParser();

            FileReader fileReader = new FileReader(jsonArrayString);
            snapshotsArray = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } catch (Exception e) {
            snapshotsArray = new JSONArray();
        }
    }

    public void saveState() {
        try {
            FileWriter fileWriter = new FileWriter(jsonArrayString);
            snapshotsArray.writeJSONString(fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTransaction(Card.Memento snapshot) {
        JSONObject snapshotObject = new JSONObject();
        snapshotObject.put("id",snapshot.id);
        if(snapshot.beforeBalance.isSameCurrency(snapshot.transactionAmount)) {
            snapshotObject.put("beforeBalance",snapshot.beforeBalance.toString());
            snapshotObject.put("transactionAmount",snapshot.transactionAmount.toString());
            snapshotObject.put("afterBalance",snapshot.afterBalance.toString());
        } else {
            snapshotObject.put("beforeBalance",snapshot.beforeBalance.toString());
            snapshotObject.put("notConvertedTransactionAmount",snapshot.transactionAmount.toString());
            snapshotObject.put("transactionAmount",snapshot.afterBalance.minus(snapshot.beforeBalance).toString());
            snapshotObject.put("afterBalance",snapshot.afterBalance.toString());
        }
        snapshotsArray.add(snapshotObject);
    }

    @Override
    public Transaction getTransaction(String id) {

        JSONObject transactionJSON = new JSONObject();

        for(Object transactionObject : snapshotsArray) {
            transactionJSON = (JSONObject) transactionObject;
            String tempId = (String) transactionJSON.get("id");
            if(Objects.equals(id, tempId)) {
                break;
            }
        }

        Money beforeBalance = Money.parse((String)transactionJSON.get("beforeBalance"));
        Money afterBalance = Money.parse((String)transactionJSON.get("afterBalance"));

        if(afterBalance.compareTo(beforeBalance) > 0) {
            return new ReduceTransaction(afterBalance.minus(beforeBalance));
        } else {
            return new AddTransaction(beforeBalance.minus(afterBalance));
        }
    }

    @Override
    public void deleteTransaction(String id) {
        JSONObject transactionJSON = new JSONObject();

        for(Object transactionObject : snapshotsArray) {
            transactionJSON = (JSONObject) transactionObject;
            String tempId = (String) transactionJSON.get("id");
            if(Objects.equals(id, tempId)) {
                break;
            }
        }

        snapshotsArray.remove(transactionJSON);
    }
}
