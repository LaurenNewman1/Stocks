import java.util.ArrayList;
import java.util.List;

public class Bank {

    List<Transaction> txns;
    int profit;

    Bank() {
        txns = new ArrayList<>();
        profit = 0;
    }

    Bank(List<Transaction> txns, int profit) {
        this.txns = new ArrayList<>(txns);
        this.profit = profit;
    }
}
