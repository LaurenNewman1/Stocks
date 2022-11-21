import java.util.*;
/*
This file contains the implementations of all the tasks associated with Problem 3.
*/
public class Problem3 {
     /* This function is used to calculate the set of transactions that produces the maximum possible profit taking the cooldown period c into consideration using the Brute force paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    c- coolDown period.
    @returns : A transaction containing a list of k transactions that contains the information regarding the maximum profit generating transaction in the form
   [<Stockname, Buyday, SellDay>]
    */
    private static Bank recursive(int stocks[][], int c, int i, int j, boolean buyFlag, List<Transaction> txns) {
        int m = stocks.length;
        int n = stocks[0].length;
        //Base case, if we reach the final stock or the final day, return the List of transactions computed so far.
        if (i >= m || j >= n)
            return new Bank(txns, 0);
        int profit = 0;
        List<Transaction> mTxns = new ArrayList<>(txns);
        //Here we have 2 choices. We either cannot sell because we have not bought the stock or we are in a cooldown period and the other choice is we sell a stock.
        //If we have cannot sell then we check for 2 conditions.
        if (buyFlag) {
            for (int k = i; k < m; k++) {
                //we are in a coolDown period so we cannot do anything.
                Bank notBuy = recursive(stocks, c, k, j + 1, true, txns);
                List<Transaction> buyTxns = new ArrayList<>(txns);
                buyTxns.add(new Transaction(k, j, 0, -stocks[k][j]));
                //We are buying the stock. 
                Bank buy = recursive(stocks, c, k, j + 1, false, buyTxns);
                buy.profit -= stocks[k][j];
                //Compare the profits and keep track of the profits and optimal set of transactions for both these scenarios.
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
        //We are selling a stock we have already bought.
        else {
            //Profit we get if we don't perform the transactions, this will help us to track if there are better sell Days in the future.
            Bank notSell = recursive(stocks, c, i, j + 1, false, txns);
            int lastIndex = txns.size() - 1;
            List<Transaction> sellTxns = new ArrayList<>(txns);
            sellTxns.add(new Transaction(i, txns.get(lastIndex).buyDay, j, stocks[i][j] - stocks[i][txns.get(lastIndex).buyDay]));
            sellTxns.remove(lastIndex);
            //Profit we get by selling the stock. 
            Bank sell = recursive(stocks, c, 0, j + c + 1, true, sellTxns);
            sell.profit += stocks[i][j];
            //We take the maximum profit of both these choices as this will maximize our overall profit.
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
        return opt.txns;
    }

    /* This function is used to calculate the set of transactions that produces the maximum possible profit taking the 
    cooldown period c into consideration using the Dynamic programming bottom up paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    c- coolDown period.
    @returns : A transaction containing a list of k transactions that contains the information regarding the maximum profit generating transaction in the form
   [<Stockname, Buyday, SellDay>]
    */

    public static List<Transaction> dynamic1(int[][] stocks, int c) {
        int m = stocks.length;
        int n = stocks[0].length;
        int[] dp = new int[n];
        Transaction[] dpTxns = new Transaction[n];
        // Since we require Dp[0] and Dp[1] where DP[0] is the best possible profit on day 1 and dp[1] is the best 
        //possbile profit on day 2 for m stocks, we calculate DP[1] for m stocks in the following code.
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
                //Here we get the prev optimal profit among all days before current day i taking the coolDown period into consideration.
                //This gives us Opt(i-c) required for our algorithm.
                if (j >= c + 1) {
                    prev = dp[j - c - 1];
                    prevTxn = j - c - 1;
                }
                for (int k = 0; k < m; k++) {
                    //Here to fill dp[day i], we check all the combinations of previous day with cooldown and all the stocks. 
                    //The below code is the implementation of max(OPT(i-1), max((Opt(i-c) or 0) + price[i] - price[j]) for j from 0 to i-1
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
    /* This function is used to calculate the set of transactions that produces the maximum possible profit taking the 
    cooldown period c into consideration using the Dynamic programming memoization paradigm with improved time complexity.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    c- coolDown period.
    @returns : A transaction containing a list of k transactions that contains the information containing the 
    maximum profit generating transactions in the form
   [<Stockname, Buyday, SellDay>]
    */
    public static List<Transaction> dynamic2Mem(int[][] stocks, int c) {
        int m = stocks.length;
        int n = stocks[0].length;
        //Here we initialize the cahce to store the result of subProblems containing the stock and day combination along with whether we buy or sell it
        //So memo is of type memo[stock][day][buy or sell].
        int[][][] memo = new int[m][n][2];
        // init
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 2; k++) {
                    memo[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }
        Bank[][][] memoTxn = new Bank[m][n][2];
        // buyFlag is 1 for buy, 0 for sell
        Bank bank = memoize(stocks, c, 0, 0, 1, memoTxn, memo);
        return bank.txns;
    }
    //This is the recursive formulation of the Brute force algortihm we have implemented above with a cache 
    //So as to reduce the number of subproblems we are solving.
    private static Bank memoize(int[][] stocks, int c, int stock, int day, int buyFlag, Bank[][][] memoTxn, int[][][] memo) {
        int m = stocks.length;
        int n = stocks[0].length;
        //Base case, if we reach the final stock or the final day, return the List of transactions computed so far.
        if (stock >= m || day >=n)
            return new Bank(new ArrayList<>(), 0);
        //If we have already encountered this case, just return the answer. 
        if (memo[stock][day][buyFlag] != Integer.MIN_VALUE)
            return new Bank(memoTxn[stock][day][buyFlag].txns, memo[stock][day][buyFlag]);
        int profit = 0;
        List<Transaction> mTxns = new ArrayList<>();
        //Here we have 2 choices. We either cannot sell because we have not bought the stock or we are in a cooldown period and the other choice is we sell a stock.
        //If we have cannot sell then we check for 2 conditions.
        if (buyFlag == 1) {
            for (int k = stock; k < m; k++) {
                //We are buying the stock. 
                Bank buy = memoize(stocks, c, k, day + 1, 0, memoTxn, memo);
                buy.profit -= stocks[k][day];
                //we are in a coolDown period so we cannot do anything.
                Bank notBuy = memoize(stocks, c, k, day + 1, 1, memoTxn, memo);
                //Compare the profits and keep track of the profits and optimal set of transactions for both these scenarios.
                if (buy.profit > profit) {
                    Transaction last = buy.txns.get(buy.txns.size() - 1);
                    buy.txns.remove(buy.txns.size() - 1);
                    buy.txns.add(new Transaction(k, day, last.sellDay, stocks[k][last.sellDay] - stocks[k][day]));
                    mTxns = buy.txns;
                    profit = buy.profit;
                }
                if (notBuy.profit > profit) {
                    mTxns = notBuy.txns;
                    profit = notBuy.profit;
                }
            }
        }
        //We are selling a stock we have already bought.
        else {
            //Profit we get by selling the stock. 
            Bank sell = memoize(stocks, c, 0, day + c + 1, 1, memoTxn, memo);
            sell.profit += stocks[stock][day];
            //Profit we get if we don't perform the transactions, this will help us to track if there are better sell Days in the future.
            Bank notSell = memoize(stocks, c, stock, day + 1, 0, memoTxn, memo);
            //We take the maximum profit of both these choices as this will maximize our overall profit.
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
        memo[stock][day][buyFlag] = profit;
        memoTxn[stock][day][buyFlag] = new Bank(mTxns, profit);
        return new Bank(mTxns, profit);
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
                    if (i > c && dp[i - c - 1] - stocks[k][i] > maxDiff[k]){
                        maxDiff[k] = dp[i - c - 1] - stocks[k][i];
                        maxDiffDay[k] = new int[]{i, i - c - 1};
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
