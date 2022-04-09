package wallet.card;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONHistoryKeeper implements HistoryKeeper{

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
        JSONArray arrayToSave;
        try {
            JSONParser jsonParser = new JSONParser();

            FileReader fileReader = new FileReader(jsonArrayString);
            arrayToSave = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } catch (Exception e) {
            arrayToSave = new JSONArray();
        }
        arrayToSave.addAll(snapshotsArray);
        try {
            FileWriter fileWriter = new FileWriter(jsonArrayString);
            arrayToSave.writeJSONString(fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTransaction(Card.Memento snapshot) {
        JSONObject snapshotObject = new JSONObject();
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
}
