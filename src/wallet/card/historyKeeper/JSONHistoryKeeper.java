package wallet.card.historyKeeper;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wallet.PropertiesStorage;
import wallet.card.Card;
import wallet.card.transaction.Transaction;
import wallet.money.Money;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class JSONHistoryKeeper implements  HistoryKeeper{

    private static final PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
    private static String jsonPathString;
    private static JSONArray currencyJSONArray;
    private static String dir = "json/";

    public static final String defaultFileName = "cardHistory";
    public static final String propertyName = "cardHistoryPath";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
    private static String dateString;

    private String getDateFromName(String name) {
        String dateString = name.substring(name.length() - 15);
        dateString = dateString.substring(0,10);
        return dateString;
    }

    JSONHistoryKeeper() {

    }
    JSONHistoryKeeper(String jsonFilePath) throws IOException, ParseException {
        jsonPathString = jsonFilePath;
        Date date = new Date();
        dateString = formatter.format(date);
        if(Objects.equals(dateString,getDateFromName(jsonPathString))) {

            JSONParser jsonParser = new JSONParser();

            FileReader fileReader = new FileReader(jsonPathString);
            currencyJSONArray = (JSONArray) jsonParser.parse(fileReader);
            fileReader.close();
        } else {
            new JSONHistoryKeeper();
        }
    }

    @Override
    public void restoreTransaction(Card card, Transaction transaction) {

    }

    @Override
    public void saveState(Money beforeBalance, Money afterBalance, Money transactionAmount) {

    }
}
