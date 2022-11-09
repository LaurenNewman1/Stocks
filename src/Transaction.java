public class Transaction {
    public int stock;
    public int buyDay;
    public int sellDay;
    public int profit;

    Transaction() {
        this.stock = 0;
        this.buyDay = 0;
        this.sellDay = 0;
        this.profit = 0;
    }

    Transaction(int stock, int buyDay, int sellDay) {
        this.stock = stock;
        this.buyDay = buyDay;
        this.sellDay = sellDay;
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
}
