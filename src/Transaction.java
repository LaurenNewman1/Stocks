import java.util.List;
/* This class provides us with a data structure that allows us to keep track of transactions across all 9 tasks. 
We have used constructor overloading to get the object with appropriate data structure containing proper tracking information.
*/
public class Transaction {
    public int stock;
    public int buyDay;
    public int sellDay;
    public int profit;
    public int[] cacheRef;
    public int prevIdx;
    //Contains different constructor overloads to suit our requirement across all 9 task implementations.
    Transaction() {
        this.stock = 0;
        this.buyDay = 0;
        this.sellDay = 0;
        this.profit = 0;
        this.cacheRef = new int[]{0, 0};
    }

    Transaction(int stock, int buyDay, int sellDay, int profit, int[] cacheRef) {
        this.stock = stock;
        this.buyDay = buyDay;
        this.sellDay = sellDay;
        this.profit = profit;
        this.cacheRef = new int[]{cacheRef[0], cacheRef[1]};
    }

    Transaction(int stock, int buyDay, int sellDay, int profit, int prevIdx) {
        this.stock = stock;
        this.buyDay = buyDay;
        this.sellDay = sellDay;
        this.profit = profit;
        this.prevIdx = prevIdx;
    }

    Transaction(int stock, int buyDay, int sellDay, int profit) {
        this.stock = stock;
        this.buyDay = buyDay;
        this.sellDay = sellDay;
        this.profit = profit;
    }

    Transaction(int profit, int buyDay) {
        this.profit = profit;
        this.buyDay = buyDay;
    }

    public void print() {
        // + 1 because they should see it starting with 1, not 0
        System.out.println((stock + 1) + " " + (buyDay + 1) + " " + (sellDay + 1));
    }
    //Here we are overriding the normal behavious of the = operator so we can easily compare different transactions
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Transaction))
            return false;
        Transaction t = (Transaction) o;
        if (this.stock == t.stock && this.buyDay == t.buyDay
                && this.sellDay == t.sellDay && this.profit == t.profit)
            return true;
        return false;
    }
    //These Getters are used to get the profit so we can easily use the transactions for comparision in the algorithms.
    public static int getProfit(Transaction[] list) {
        int total = 0;
        for (Transaction t : list) {
            if (t != null)
                total += t.profit;
        }
        return total;
    }

    public static int getProfit(List<Transaction> list) {
        int total = 0;
        for (Transaction t : list) {
            if (t != null)
                total += t.profit;
        }
        return total;
    }
}
