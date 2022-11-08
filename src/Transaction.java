public class Transaction {
    public int stock;
    public int buyDay;
    public int sellDay;
    public int profit;

    Transaction() {}

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
        stock++; buyDay++; sellDay++;
        System.out.println(stock + " " + buyDay + " " + sellDay);
    }
}
