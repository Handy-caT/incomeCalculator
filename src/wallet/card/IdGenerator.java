package wallet.card;

import wallet.card.transaction.Transaction;

import java.math.BigInteger;

public class IdGenerator {

    private BigInteger counter;
    private Card card;

    IdGenerator(Card card) {
        this.card = card;
        counter = BigInteger.ZERO;
    }

    String getId(Transaction transaction) {
        String result = card.id + transaction.moneyAmount.getCurrency().toString() +counter;
        counter = counter.add(BigInteger.ONE);
        return result;
    }


}
