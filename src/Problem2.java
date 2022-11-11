public class Problem2 {

    //private static Transaction[] bruteTrans;

    public static Transaction[] bruteForce(int[][] stocks, int k) {
        Transaction[] opt = bruteRecur(stocks, 0, k, 0, new Transaction[k], new Transaction[k]);
        for (Transaction t : opt)
            t.print();
        return opt;
    }
    private static Transaction[] bruteRecur(int[][] stocks, int lastSellDay, int k, int soFar, Transaction[] txns, Transaction[] finTxns) {
        if (k == 0) {
            if (soFar > Transaction.getProfit(finTxns)) {
                finTxns = txns;
            }
            return finTxns;
        }
        int m = stocks.length;
        int n = stocks[0].length;
        for (int j1 = lastSellDay; j1 < n; j1++) {
            for (int j2 = j1 + 1; j2 < n; j2++) {
                Transaction maxTrans = new Transaction();
                for (int i = 0; i < m; i++) {
                    if (stocks[i][j2] - stocks[i][j1] > maxTrans.profit) {
                        maxTrans = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1]);
                    }
                }
                txns[k - 1] = maxTrans;
                finTxns = bruteRecur(stocks, j2, k - 1, soFar + maxTrans.profit, txns, finTxns);
            }
        }
        return finTxns;
    }

    public static Transaction[] dynamic1(int[][] stocks, int k) {
        throw new UnsupportedOperationException();
    }

    public static Transaction[] dynamic2Mem(int[][] stocks, int k) {
        throw new UnsupportedOperationException();
    }

    public static Transaction[] dynamic2BU(int[][] stocks, int k) {
        throw new UnsupportedOperationException();
    }
}
