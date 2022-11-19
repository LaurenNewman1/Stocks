import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem3 {

    private static Bank recursive(int stocks[][], int c, int i, int j, boolean buyFlag, List<Transaction> txns) {
        int m = stocks.length;
        int n = stocks[0].length;
        if (i >= m || j >= n)
            return new Bank(txns, 0);
        int profit = 0;
        List<Transaction> mTxns = new ArrayList<>(txns);
        if (buyFlag) {
            for (int k = i; k < m; k++) {
                Bank notBuy = recursive(stocks, c, k, j + 1, true, txns);
                List<Transaction> buyTxns = new ArrayList<>(txns);
                buyTxns.add(new Transaction(k, j, 0, -stocks[k][j]));
                Bank buy = recursive(stocks, c, k, j + 1, false, buyTxns);
                buy.profit -= stocks[k][j];
                if (buy.profit > profit) {
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
            Bank notSell = recursive(stocks, c, i, j + 1, false, txns);
            int lastIndex = txns.size() - 1;
            List<Transaction> sellTxns = new ArrayList<>(txns);
            sellTxns.add(new Transaction(i, txns.get(lastIndex).buyDay, j, stocks[i][j] - stocks[i][txns.get(lastIndex).buyDay]));
            sellTxns.remove(lastIndex);
            Bank sell = recursive(stocks, c, 0, j + c, true, sellTxns);
            sell.profit += stocks[i][j];
            if (sell.profit > profit) {
                mTxns = sell.txns;
                profit = sell.profit;
            }
            if (notSell.profit > profit) {
                mTxns = notSell.txns;
                profit = notSell.profit;
            }
        }
        return new Bank(mTxns, profit);
    }

    public static List<Transaction> bruteForce(int[][] stocks, int c) {
        Bank opt = recursive(stocks, c, 0, 0, true, new ArrayList<>());
        System.out.print("Profit: " + opt.profit);
        return opt.txns;
    }

    public static List<Transaction> dynamic1(int[][] stocks, int c) {
        int m = stocks.length;
        int n = stocks[0].length;
        int[] dp = new int[n];
        Transaction[] dpTxns = new Transaction[n];
        // first sell
        for (int k = 0; k < m; k++) {
            int profit = stocks[k][1] - stocks[k][0];
            if (profit > dp[1]) {
                dp[1] = profit;
                dpTxns[1] = new Transaction(k, 0, 1, stocks[k][1] - stocks[k][0], 0);
            }
        }
        for (int i = 2; i < n; i++) {
            dp[i] = dp[i - 1];
            dpTxns[i] = dpTxns[i - 1];
            for (int j = 0; j < i; j++) {
                int prev = 0;
                int prevTxn = 0;
                if (j >= c) {
                    prev = dp[j - c];
                    prevTxn = j - c;
                }
                for (int k = 0; k < m; k++) {
                    int profit = prev + stocks[k][i] - stocks[k][j];
                    if (profit > dp[i]) {
                        dp[i] = profit;
                        dpTxns[i] = new Transaction(k, j, i, stocks[k][i] - stocks[k][j], prevTxn);
                    }
                }
            }
        }
        return traverseFromLast(dpTxns);
    }

    public static List<Transaction> dynamic2Mem(int[][] stocks, int c) {
        throw new UnsupportedOperationException();
    }

    private static void memoize (int[][] stocks, int c) {

    }

    public static List<Transaction> dynamic2BU(int[][] stocks, int c) {
        int m = stocks.length;
        int n = stocks[0].length;
        int[] dp = new int[n];
        Transaction[] dpTxns = new Transaction[n];
        int[][] dpK = new int[m][n];
        Transaction[][] dpKTxns = new Transaction[m][n];
        int[] maxDiff = new int[m];
        Arrays.fill(maxDiff, Integer.MIN_VALUE);
        int[][] maxDiffDay = new int[m][2];
        for (int i = 0; i < n; i++) {
            if (i < c) {
                for (int k = 0; k < m; k++) {
                    if (-stocks[k][i] > maxDiff[k]) {
                        maxDiff[k] = -stocks[k][i];
                        maxDiffDay[k] = new int[]{i, 0};
                    }
                }
            }
            if (i == 1) {
                for (int k = 0; k < m; k++) {
                    int profit = stocks[k][1] - stocks[k][0];
                    if (profit > dp[1]) {
                        dpTxns[1] = new Transaction(k, 0, 1, stocks[k][1] - stocks[k][0], 0);
                        dp[1] = profit;
                    }
                    if (profit > dpK[k][1]) {
                        dpKTxns[k][1] = new Transaction(k, 0, 1, stocks[k][1] - stocks[k][0], 0);
                        dpK[k][1] = profit;
                    }
                }
            }
            else if (i > 1) {
                for (int k = 0; k < m; k++) {
                    if (maxDiff[k] + stocks[k][i] > dpK[k][i - 1]) {
                        dpK[k][i] = maxDiff[k] + stocks[k][i];
                        dpKTxns[k][i] = new Transaction(k, maxDiffDay[k][0], i, stocks[k][i] - stocks[k][maxDiffDay[k][0]], maxDiffDay[k][1]);
                    }
                    else {
                        dpK[k][i] = dpK[k][i - 1];
                        dpKTxns[k][i] = dpKTxns[k][i - 1];
                    }
                    if (dpK[k][i] > dp[i]) {
                        dp[i] = dpK[k][i];
                        dpTxns[i] = dpKTxns[k][i];
                    }
                    if (dp[i - c] - stocks[k][i] > maxDiff[k]){
                        maxDiff[k] = dp[i - c] - stocks[k][i];
                        maxDiffDay[k] = new int[]{i, i - c};
                    }
                }
            }
        }
        return traverseFromLast(dpTxns);
    }

    private static List<Transaction> traverseFromLast(Transaction dpTxn[]) {
        List<Transaction> txns = new ArrayList<>();
        Transaction last = dpTxn[dpTxn.length - 1];
        while (last != null) {
            txns.add(last);
            last = dpTxn[last.prevIdx];
        }
        return txns;
    }
}
