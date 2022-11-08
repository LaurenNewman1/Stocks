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
        this.stock = stock + 1;
        this.buyDay = buyDay + 1;
        this.sellDay = sellDay + 1;
        this.profit = profit;
    }

    public void print() {
        System.out.println(stock + " " + buyDay + " " + sellDay);
    }
}
