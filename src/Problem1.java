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

    static Cache A;
    static Transaction[] opts;
    public static Transaction dynamicMem(int[][] stocks) {
        int m = stocks.length;
        int n = stocks[0].length;
        // Init
        A = new Cache(m, n);
        opts = new Transaction[m];
        for (int i = 0; i < m; i++) {
            opts[i] = new Transaction();
        }
        // Recursive
        memoize(stocks, m - 1, n - 1);
        // Find single optimal over all the stocks
        Transaction opt = opts[0];
        for (int i = 1; i < m; i++) {
            if (opts[i].profit > opt.profit)
                opt = opts[i];
        }
        return opt;
    }
    private static void memoize(int[][] stocks, int i, int j) {
        if (j == 0) {
            A.minCost[i][j] = stocks[i][0];
            A.maxProfit[i][j] = -1 * stocks[i][0];
            if (i > 0)
                memoize(stocks, i - 1, stocks[0].length - 1);
        }
        else {
            // recurse if nothing to compare with
            if (A.minCost[i][j - 1] <= 0 && j > 0) {
                memoize(stocks, i, j - 1);
            } else if (A.maxProfit[i][j - 1] <= 0 && j > 0) {
                memoize(stocks, i, j - 1);
            }
            // Select the one that will be traded
            if (A.minCost[i][j - 1] <= stocks[i][j]) {
                A.minCost[i][j] = A.minCost[i][j - 1];
            } else { // Buy it
                A.minCost[i][j] = stocks[i][j];
                opts[i].stock = i;
                opts[i].buyDay = j;
            }
            // Select the final purhcase
            if (A.maxProfit[i][j - 1] >= stocks[i][j] - A.minCost[i][j - 1]) {
                A.maxProfit[i][j] = A.maxProfit[i][j - 1];
            } else { // Sell current and buy this
                A.maxProfit[i][j] = stocks[i][j] - A.minCost[i][j - 1];
                opts[i].sellDay = j;
                opts[i].profit = A.maxProfit[i][j];
            }
        }
    }

    public static Transaction dynamicBU(int[][] stocks) {
        int days = stocks[0].length;
        if (days < 2)
            return null;
        int maxProfit = 0;
        Transaction txn = new Transaction();
        for (int i = 0; i < stocks.length; i++) {
            int previousBuyValue = -stocks[i][0];
            int previousSellValue = 0;
            int currentBuyValue = 0;
            int currentSellValue = 0;
            int buyDay = 0;
            int saleDay = 0;
            int tempPurchaseDay = 0;
            for (int day = 1; day < days; day++) {
                currentSellValue = Math.max(previousSellValue, previousBuyValue + stocks[i][day]);
                if (currentSellValue > previousSellValue) {
                    buyDay = tempPurchaseDay;
                    saleDay = day;
                }
                currentBuyValue = Math.max(previousBuyValue, -stocks[i][day]);
                if (currentBuyValue > previousBuyValue)
                    tempPurchaseDay = day;
                previousBuyValue = currentBuyValue;
                previousSellValue = currentSellValue;
            }
            if (previousSellValue > maxProfit) {
                maxProfit = previousSellValue;
                txn = new Transaction(i, buyDay, saleDay, stocks[i][saleDay] - stocks[i][buyDay]);
            }
        }
        return txn;
    }
}
