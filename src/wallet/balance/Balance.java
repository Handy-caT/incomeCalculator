package wallet.balance;

public class Balance {

    float balance;

    public Balance() {
        balance = 0;
    }
    public Balance(float startBalance) {
        balance = startBalance;
    }

    public boolean receive(float moneyAmount) {
        balance += moneyAmount;
        return true;
    }
    public boolean spend(float moneyAmount) {
        balance -= moneyAmount;
        return true;
    }
    public float getBalance() {
        return balance;
    }

}
