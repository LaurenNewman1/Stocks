import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem2 {

    public static Transaction[] bruteForce(int[][] stocks, int k) {
        Transaction[] opt = bruteRecur(stocks, 0, k, 0, new Transaction[k], new Transaction[k]);
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

    static Cache A;
    public static Transaction[] dynamic2Mem(int[][] stocks, int k) {
        int m = stocks.length;
        int n = stocks[0].length;
        int[][][][] dpProfit = new int[m][n][k+1][2];
        // init
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int K = 0; K < k+1; K++) {
                    for (int l = 0; l < 2; l++) {
                        dpProfit[i][j][K][l] = Integer.MIN_VALUE;
                    }
                }
            }
        }
        Bank[][][][] dpTxn = new Bank[m][n][k+1][2];
        Bank bank = memoize(stocks, k, 0, 0, 1, dpTxn, dpProfit);
        return bank.txns.toArray(new Transaction[k]);
    }

    private static Bank memoize(int[][] stocks, int k, int stock, int day, int buyFlag, Bank[][][][] memoTxn, int[][][][] memo) {
        int m = stocks.length;
        int n = stocks[0].length;
        if (k == 0 || day >=n)
            return new Bank(new ArrayList<>(), 0);
        if (memo[stock][day][k][buyFlag] != Integer.MIN_VALUE)
            return new Bank(memoTxn[stock][day][k][buyFlag].txns, memo[stock][day][k][buyFlag]);
        int profit = 0;
        List<Transaction> mTxns = new ArrayList<>();
        if (buyFlag == 1) {
            for (int i = stock; i < m; i++) {
                Bank buy = memoize(stocks, k, i, day + 1, 0, memoTxn, memo);
                buy.profit -= stocks[i][day];
                Bank notBuy = memoize(stocks, k, i, day + 1, 1, memoTxn, memo);
                if (buy.profit > profit) {
                    Transaction last = buy.txns.get(buy.txns.size() - 1);
                    buy.txns.remove(buy.txns.size() - 1);
                    buy.txns.add(new Transaction(i, day, last.sellDay, stocks[i][last.sellDay] - stocks[i][day]));
                    mTxns = buy.txns;
                    profit = buy.profit;
                }
                if (notBuy.profit > profit) {
                    mTxns = notBuy.txns;
                    profit = notBuy.profit;
                }
            }
        }
        else {
            Bank sell = memoize(stocks, k - 1, 0, day, 1, memoTxn, memo);
            sell.profit += stocks[stock][day];
            Bank notSell = memoize(stocks, k, stock, day + 1, 0, memoTxn, memo);
            if (sell.profit > profit) {
                sell.txns.add(new Transaction(stock, 0, day, stocks[stock][day] - stocks[stock][0]));
                mTxns = sell.txns;
                profit = sell.profit;
            }
            if (notSell.profit > profit) {
                mTxns = notSell.txns;
                profit = notSell.profit;
            }
        }
        memo[stock][day][k][buyFlag] = profit;
        memoTxn[stock][day][k][buyFlag] = new Bank(mTxns, profit);
        return new Bank(mTxns, profit);
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
