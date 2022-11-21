import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
This file contains the implementations of all the tasks associated with Problem 2.
*/
public class Problem2 {
    /* This function is used to calculate the k-transaction that produces the maximum possible profit using the Brute force paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    k- Number of transactions allowed.
    @returns : A transaction containing a list of k transactions that contains the information regarding the maximum profit generating transaction in the form
   [<Stockname, Buyday, SellDay>]
    */
    public static Transaction[] bruteForce(int[][] stocks, int k) {
        if (k >= stocks[0].length)
            k = stocks[0].length - 1;
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
        //This code is used to check every combination of non-overlapping transactions of m stocks from the last sell Date. 
        for (int j1 = lastSellDay; j1 < n; j1++) {
            for (int j2 = j1 + 1; j2 < n; j2++) {
                Transaction maxTrans = new Transaction();
                for (int i = 0; i < m; i++) {
                    //If the newer Transaction will result in a better profit than the previous transaction with last sell Day, 
                    //We ignore the last sell Day, and update our set of transactions with this transaction data.
                    if (stocks[i][j2] - stocks[i][j1] > maxTrans.profit) {
                        maxTrans = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1]);
                    }
                }
                txns[k - 1] = maxTrans;
                //Here we got the newest maxTransaction, so we decrease our k count to reflect the remaining transactions we can make
                //And check for them.
                finTxns = bruteRecur(stocks, j2, k - 1, soFar + maxTrans.profit, txns, finTxns);
            }
        }
        return finTxns;
    }

    /* This function is used to calculate the k-transaction that produces the maximum possible profit using the dynamic programming paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    k- Number of transactions allowed.
    @returns : A transaction containing a list of k transactions that contains the information regarding the maximum profit generating transaction in the form
   [<Stockname, Buyday, SellDay>]
    */

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
                        /*
                        While filling the table, we need 
                       -> 1 ) max(price[stock][currentday] - price[stock][previousDays] + dp[(k-1)th transaction][previousDays])
						 where previoudDay = (for previousDays from day 0 to currentDay)
                         That is what is being calculated below and stored in totalProfit. 
                        */
                        int currProfit = stocks[i][j2] - stocks[i][j1] + dpProfit[t - 1][j1];
                        if (currProfit > totProfit) {
                            totProfit = currProfit;
                            maxTrans = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1], new int[]{t - 1, j1});
                        }
                    }
                }
                //In our algorithm, we check the maximum( cond -> 1(above), dp[kth transaction][day n-1])
                //The following code is checking this condition and filling up dp[t][j2] accordingly with correct value
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

    /* This function is used to calculate the k-transaction that produces the maximum possible profit using the dynamic programming memoization paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    k- Number of transactions allowed.
    @returns : A transaction containing a list of k transactions that contains the information regarding the maximum profit generating transaction in the form
   [<Stockname, Buyday, SellDay>]
    */

    static Cache A;
    public static Transaction[] dynamic2Mem(int[][] stocks, int k) {
        int m = stocks.length;
        int n = stocks[0].length;
        //Here in the memoziation, we will memoize all the choices that we were considering in the brute force 
        //So we will drastically reduce the number of subproblems.
        //The memoized sub-problem is of the form dpProfit[stock][currentDay][k][buy or sell]
        int[][][][] dpProfit = new int[m][n][k+1][2];
        // initialize to Minimum value.
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
    //This is a recursive function that will generate the required transactions.
    private static Bank memoize(int[][] stocks, int k, int stock, int day, int bought, Bank[][][][] memoTxn, int[][][][] memo) {
        int m = stocks.length;
        int n = stocks[0].length;
        //Base case : If no transactions are left, or if we reach the final day.
        if (k == 0 || day == n)
            return new Bank(new ArrayList<>(), 0);
        List<Transaction> maxTxns = new ArrayList<>();
        //If we have not computed a buy/sell choice for a particular stock and day.
        if (memo[stock][day][k][bought] == Integer.MIN_VALUE) {
            //If we have already bought some stock x, on previous day we can either sell it or do nothing. 
            if (bought == 1) {
                //Calculate the profit we get if we sell it. Decrease the k-count, because we are making a transaction and bought set to False.
                Bank sell = memoize(stocks, k - 1, 0, day, bought == 1 ? 0 : 1, memoTxn, memo);
                sell.profit += stocks[stock][day];
                //Calculate the profit we get if we don't sell it. In this case, we just check the previous profit. 
                Bank noSell = memoize(stocks, k, stock, day + 1, bought, memoTxn, memo);
                //If we get more profit by making the transaction in the previous step, we consider it or we just conisder the other previous transaction information.
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
            //If we have not bought the stock, we need to check which stock is optimal to buy on a given day among m stocks.
            else {
                int maxB = 0;
                for (int i = 0; i < m; i++) {
                    //Scenario to check which stock is optimal to buy on a given day.
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
                //Check the profit we can make if we don't buy a stock. This is used to handle the look ahead cases where we skip days buy days that are not profitable.  
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

    /* This function is used to calculate the k-transaction that produces the maximum possible profit using the Dynamic programming Bottom Up paradigm with improved time complexity.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    k- Number of transactions allowed.
    @returns : A transaction containing a list of k transactions that contains the information regarding the maximum profit generating transaction in the form
   [<Stockname, Buyday, SellDay>]
    */

    public static Transaction[] dynamic2BU(int[][] stocks, int k) {
        int m = stocks.length;
        int n = stocks[0].length;
        int dpProfit[][] = new int[k + 1][n];
        Transaction dpTxn[][] = new Transaction[k + 1][n];

//       max(OPT[k][j-1], price[j] + maxDiff)    if k > 0 and j > 0
//		where maxDiff = max(maxDiff, OPT[k-1][j] - price[j] calculated updated each time for each stock.

        for (int t = 1; t < k + 1; t++) {
            int[] maxDiff = new int[m];
            int[] maxDiffDay = new int[m];
            for (int i = 0; i < m; i++)
                //Initlize maxDiff of each stock to the price on Day 1, since we cannot have any profit on Day1.
                maxDiff[i] = -1 * stocks[i][0];
            for (int j = 1; j < n; j++) {
                int totProfit = 0;
                Transaction maxTrans = new Transaction();
                for (int i = 0; i < m; i++) {
                    //In our algorithm, we need to calculate the profit generated by k-1 transactions on day n + current day transaction as
                    //Stock[i][j] + maxDiff[i]. This is being calcuated below.
                    int currProfit = stocks[i][j] + maxDiff[i];
                    if (currProfit > totProfit) {
                        totProfit = currProfit;
                        maxTrans = new Transaction(i, maxDiffDay[i], j, stocks[i][j] - stocks[i][maxDiffDay[i]], new int[]{t - 1, maxDiffDay[i]});
                    }
                    //We also need to Update maxDiff as we move through each day ass
                    //maxDiff = max(maxDiff, dpProfit[k-1][j] - stock[i][j]). 
                    int currDiff = dpProfit[t - 1][j] - stocks[i][j];
                    if (currDiff > maxDiff[i]) {
                        maxDiff[i] = currDiff;
                        maxDiffDay[i] = j;
                    }
                }
                //Here we compare whether the new Transaction with selling on current day + previous k-1 transactions has a better profit than 
                //or the profit we already got on kth transactions on day j-1. We fill the table with maximum of both these values.
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
    //This function is used to process the transactions so that it is in the required output format as specified in the requirement document.
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
