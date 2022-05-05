package wallet.card.transaction;

import wallet.card.Card;

public interface Transaction {

    void execute(Card card);

}
