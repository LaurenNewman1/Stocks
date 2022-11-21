import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
This class produces a data structure that is used to track and store the transactions 
@params: 
List<Transaction>(optional) : A list of transactions as defined by the Transaction class.
int profit(optional) : A profit that needs to be stored in the data Structure.
@returns : A new object of Type Bank
*/
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
