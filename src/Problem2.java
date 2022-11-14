public class Problem2 {

    public static Transaction[] bruteForce(int[][] stocks, int k) {
        Transaction[] opt = bruteRecur(stocks, 0, k, 0, new Transaction[k], new Transaction[k]);
        for (Transaction t : opt)
            t.print();
        return opt;
    }
    private static Transaction[] bruteRecur(int[][] stocks, int lastSellDay, int k, int soFar, Transaction[] txns, Transaction[] finTxns) {
        if (k == 0) {
            if (soFar > Transaction.getProfit(finTxns)) {
                finTxns = txns.clone();
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
        int m = stocks.length;
        int n = stocks[0].length;
        int dpProfit[][] = new int[k + 1][n];
        Transaction dpTxn[][] = new Transaction[k + 1][n];
        for (int t = 1; t < k + 1; t++) {
            for (int j2 = 1; j2 < n; j2++) {
                int totProfit = 0;
                Transaction maxTrans = new Transaction();
                for (int j1 = 0; j1 < j2; j1++) {
                    for (int i = 0; i < m; i++) {
                        int currProfit = stocks[i][j2] - stocks[i][j1] + dpProfit[t - 1][j1];
                        if (currProfit > totProfit) {
                            totProfit = currProfit;
                            maxTrans = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1], new int[]{t - 1, j1});
                        }
                    }
                }
                if (totProfit > dpProfit[t][j2 - 1]) {
                    dpProfit[t][j2] = totProfit;
                    dpTxn[t][j2] = maxTrans;
                } else {
                    dpProfit[t][j2] = dpProfit[t][j2 - 1];
                    dpTxn[t][j2] = dpTxn[t][j2 - 1];
                }
            }
        }
        return traverseFromLast(dpTxn, k);
    }

    public static Transaction[] dynamic2Mem(int[][] stocks, int k) {
        throw new UnsupportedOperationException();
    }

    public static Transaction[] dynamic2BU(int[][] stocks, int k) {
        int m = stocks.length;
        int n = stocks[0].length;
        int dpProfit[][] = new int[k + 1][n];
        Transaction dpTxn[][] = new Transaction[k + 1][n];
        for (int t = 1; t < k + 1; t++) {
            int[] maxDiff = new int[m];
            int[] maxDiffDay = new int[m];
            for (int i = 0; i < m; i++)
                maxDiff[i] = -1 * stocks[i][0];
            for (int j = 1; j < n; j++) {
                int totProfit = 0;
                Transaction maxTrans = new Transaction();
                for (int i = 0; i < m; i++) {
                    int currProfit = stocks[i][j] + maxDiff[i];
                    if (currProfit > totProfit) {
                        totProfit = currProfit;
                        maxTrans = new Transaction(i, maxDiffDay[i], j, stocks[i][j] - stocks[i][maxDiffDay[i]], new int[]{t - 1, maxDiffDay[i]});
                    }
                    int currDiff = dpProfit[t - 1][j] - stocks[i][j];
                    if (currDiff > maxDiff[i]) {
                        maxDiff[i] = currDiff;
                        maxDiffDay[i] = j;
                    }
                }
                if (totProfit > dpProfit[t][j - 1]) {
                    dpProfit[t][j] = totProfit;
                    dpTxn[t][j] = maxTrans;
                } else {
                    dpProfit[t][j] = dpProfit[t][j - 1];
                    dpTxn[t][j] = dpTxn[t][j - 1];
                }
            }
        }
        return traverseFromLast(dpTxn, k);
    }

    private static Transaction[] traverseFromLast(Transaction dpTxn[][], int k) {
        Transaction[] txns = new Transaction[k];
        Transaction last = dpTxn[k][dpTxn[0].length - 1 ];
        int index = 0;
        while (last != null) {
            txns[index] = last;
            last = dpTxn[last.cacheRef[0]][last.cacheRef[1]];
            index++;
        }
        return txns;
    }
}
