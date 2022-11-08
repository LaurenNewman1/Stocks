public class Problem1 {

    public static Transaction bruteForce(int[][] stocks) {
        int m = stocks.length;
        int n = stocks[0].length;
        Transaction opt = new Transaction(0, 0, 0, 0);
        for (int i = 0; i < m; i++) {
            for (int j1 = 0; j1 < n; j1++) {
                for (int j2 = j1 + 1; j2 < n; j2++) {
                    if (stocks[i][j2] - stocks[i][j1] > opt.profit) {
                        opt = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1]);
                    }
                }
            }
        }
        return opt;
    }

    public static Transaction greedy(int[][] stocks) {
        int m = stocks.length;
        int n = stocks[0].length;
        Transaction opt = new Transaction(0, 0, 0, 0);
        for (int i = 0; i < m; i++) {
            Transaction min = new Transaction(stocks[i][0], 0);
            for (int j = 1; j < n; j++) {
                if (stocks[i][j] < min.profit) {
                    min = new Transaction(stocks[i][j], j);
                }
                else if (stocks[i][j] - min.profit > opt.profit) {
                    opt = new Transaction(i, min.buyDay, j, stocks[i][j] - min.profit);
                }
            }
        }
        return opt;
    }

    public static Transaction dynamicMem(int[][] stocks) {
        throw new UnsupportedOperationException();
    }

    public static Transaction dynamicBU(int[][] stocks) {
        int m = stocks.length;
        int n = stocks[0].length;
        int maxProfitBeforeBuy[][] = new int[m][n]; // always negative
        int maxProfitAfterBuy[][] = new int[m][n];
        Transaction opt = new Transaction();
        for (int i = 0; i < m; i++) {
            maxProfitBeforeBuy[i][0] = -1 * stocks[i][0];
            maxProfitAfterBuy[i][0] = -1 * stocks[i][0];
            for (int j = 1; j < n; j++) {
                if (maxProfitBeforeBuy[i][j - 1] > -1 * stocks[i][j]) {
                    maxProfitBeforeBuy[i][j] = maxProfitBeforeBuy[i][j - 1];
                }
                else { // Buy it
                    maxProfitBeforeBuy[i][j] = -1 * stocks[i][j];
                    opt.stock = i;
                    opt.buyDay = j;

                }
                if (maxProfitAfterBuy[i][j - 1] > maxProfitBeforeBuy[i][j - 1] + stocks[i][j]) {
                    maxProfitAfterBuy[i][j] = maxProfitAfterBuy[i][j - 1];
                }
                else { // Sell current and buy this
                    maxProfitAfterBuy[i][j] = maxProfitBeforeBuy[i][j - 1] + stocks[i][j];
                    opt.sellDay = j;
                    opt.profit = maxProfitAfterBuy[i][j];
                }
            }
        }
        return opt;
    }
}
