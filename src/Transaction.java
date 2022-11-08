public class Transaction {
    public int stock;
    public int buyDay;
    public int sellDay;

    Transaction() {}

    Transaction(int stock, int buyDay, int sellDay) {
        this.stock = stock;
        this.buyDay = buyDay;
        this.sellDay = sellDay;
    }

    public void print() {
        System.out.println(stock + " " + buyDay + " " + sellDay);
    }
}
