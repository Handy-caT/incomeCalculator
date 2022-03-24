package wallet.money;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

public class CurrencyUpdaterWeb implements CurrencyUpdaterProvider {
    WebClient webClient;

    CurrencyUpdaterWeb() {
        webClient = new WebClient(BrowserVersion.CHROME);

    }

    @Override
    public HashMap<String, BigDecimal> getCurrencyHash(String currencyName) {
        return null;
    }

    @Override
    public BigDecimal getRatio(String currencyFrom, String currencyTo)  {
        try {
            HtmlPage page = webClient.getPage("https://www.nbrb.by/api/exrates/rates/USD?parammode=2");
            page.getAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigDecimal getDecimalPlaces(String currencyString) {
        return null;
    }

}
