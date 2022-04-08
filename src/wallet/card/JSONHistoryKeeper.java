package wallet.card;

import org.json.simple.JSONObject;

public class JSONHistoryKeeper implements HistoryKeeper{

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
    }
}
