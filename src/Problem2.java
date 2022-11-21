import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* This function is used to calculate the k-transaction that produces the maximum possible profit using the Brute force paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    k- Number of transactions allowed.
    @returns : A transaction containing a list of k transactions that contains the information regarding the maximum profit generating transaction in the form
   [<Stockname, Buyday, SellDay>]
    */
public class Problem2 {

    public static Transaction[] bruteForce(int[][] stocks, int k) {
        //Call to the recursion function.
        Transaction[] opt = bruteRecur(stocks, 0, k, 0, new Transaction[k], new Transaction[k]);
        return opt;
    }
    private static Transaction[] bruteRecur(int[][] stocks, int lastSellDay, int k, int soFar, Transaction[] txns, Transaction[] finTxns) {
        //This is the base case of our brute force recursion, if no transactions are allowed, then return the 
        //set of best transactions computed so Far.
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
        Bank bank = memoize(stocks, k, 0, 0, 0, dpTxn, dpProfit);
        return bank.txns.toArray(new Transaction[k]);
    }

    private static Bank memoize(int[][] stocks, int k, int stock, int day, int bought, Bank[][][][] memoTxn, int[][][][] memo) {
        int m = stocks.length;
        int n = stocks[0].length;
        if (k == 0 || day == n)
            return new Bank(new ArrayList<>(), 0);
        List<Transaction> maxTxns = new ArrayList<>();
        if (memo[stock][day][k][bought] == Integer.MIN_VALUE) {
            if (bought == 1) {
                Bank sell = memoize(stocks, k - 1, 0, day, bought == 1 ? 0 : 1, memoTxn, memo);
                sell.profit += stocks[stock][day];
                Bank noSell = memoize(stocks, k, stock, day + 1, bought, memoTxn, memo);
                if (sell.profit > noSell.profit) {
                    sell.txns.add(new Transaction(stock, 0, day, 0));
                    maxTxns = new ArrayList<>(sell.txns);
                }
                else {
                    maxTxns = new ArrayList<>(noSell.txns);
                }
                memo[stock][day][k][bought] = Math.max(sell.profit, noSell.profit);
                memoTxn[stock][day][k][bought] = new Bank(new ArrayList<>(maxTxns), memo[stock][day][k][bought]);
            }
            else {
                int maxB = 0;
                for (int i = 0; i < m; i++) {
                    Bank buy = memoize(stocks, k, i, day + 1, bought == 1 ? 0 : 1, memoTxn, memo);
                    buy.profit -= stocks[i][day];
                    if (buy.profit > maxB) {
                        Transaction last = buy.txns.get(buy.txns.size() - 1);
                        buy.txns.remove(buy.txns.size() - 1);
                        buy.txns.add(new Transaction(i, day, last.sellDay, 0));
                        maxTxns = new ArrayList<>(buy.txns);
                        maxB = buy.profit;
                    }
                }
                Bank noBuy = memoize(stocks, k, stock, day + 1, bought, memoTxn, memo);
                if (noBuy.profit > maxB) {
                    maxTxns = new ArrayList<>(noBuy.txns);
                    maxB = noBuy.profit;
                }
                memo[stock][day][k][bought] = maxB;
                memoTxn[stock][day][k][bought] = new Bank(new ArrayList<>(maxTxns), maxB);
            }
        }

        return new Bank(new ArrayList<>(memoTxn[stock][day][k][bought].txns), memo[stock][day][k][bought]);
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
