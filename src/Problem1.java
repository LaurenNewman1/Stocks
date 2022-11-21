/*
This file contains the implementations of all the tasks associated with Problem 1.
*/
public class Problem1 {
    /* This function is used to calculate the transaction that produces the maximum possible profit using the Brute force paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    @returns : A transaction object that contains the information regarding the maximum profit generating transaction in the form
    <Stockname, Buyday, SellDay>
    */
    public static Transaction bruteForce(int[][] stocks) {
        int m = stocks.length;
        int n = stocks[0].length;
        Transaction opt = new Transaction(0, 0, 0, 0);
        for (int i = 0; i < m; i++) {
            for (int j1 = 0; j1 < n; j1++) {
                for (int j2 = j1 + 1; j2 < n; j2++) {
                    //For each stock i, We compare the sellPrice on day j2 with all the possible Buy Prices before day j2, represented
                    //by day j1 and if it's greater update the profit.
                    if (stocks[i][j2] - stocks[i][j1] > opt.profit) {
                        opt = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1]);
                    }
                }
            }
        }
        return opt;
    }
    /* This function is used to calculate the transaction that produces the maximum possible profit using the Greedy paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    @returns : A transaction object that contains the information regarding the maximum profit generating transaction in the form
    <Stockname, Buyday, SellDay>
    */
    public static Transaction greedy(int[][] stocks) {
        int m = stocks.length;
        int n = stocks[0].length;
        Transaction opt = new Transaction(0, 0, 0, 0);
        for (int i = 0; i < m; i++) {
            //For a new Stock, initlialize price on day 1 as the minimum Price.
            Transaction min = new Transaction(stocks[i][0], 0);
            for (int j = 1; j < n; j++) {
                //If we find a new price that is less that our current minimum price, update that minimum price to be our buy day.
                if (stocks[i][j] < min.profit) {
                    min = new Transaction(stocks[i][j], j);
                }
                //If not, try to sell the stock that was bought on a previous minimum buy data and check whether this gives better profit.
                else if (stocks[i][j] - min.profit > opt.profit) {
                    opt = new Transaction(i, min.buyDay, j, stocks[i][j] - min.profit);
                }
            }
        }
        return opt;
    }

    static Cache A;
    static Transaction[] opts;
    /* This function is used to calculate the transaction that produces the maximum possible profit using the dynamic programming memoization paradigm.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    m - number of stocks.
    n - Number of days.
    @returns : A transaction object that contains the information regarding the maximum profit generating transaction in the form
    <Stockname, Buyday, SellDay>
    */
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
    /* This function is used to calculate the transaction that produces the maximum possible profit using the Dynmaic programming bottom up approach.
    @params: 
    Stocks: A 2D array where each row represents a different stock and each column represents a different day.
    Stocks.length - number of stocks.
    days - Number of days.
    @returns : A transaction object that contains the information regarding the maximum profit generating transaction in the form
    <Stockname, Buyday, SellDay>
    */
    public static Transaction dynamicBU(int[][] stocks) {
        int days = stocks[0].length;
        //We will have 0 profit becuase we have to buy and sell on Same day.
        if (days < 2)
            return null;
        int maxProfit = 0;
        Transaction txn = new Transaction();
        for (int i = 0; i < stocks.length; i++) {
            //Assume we have bought the stock i on day 1.
            //Other initializations are assumed to be Day 1 and profit is 0.
            int previousBuyValue = -stocks[i][0];
            int previousSellValue = 0;
            int currentBuyValue = 0;
            int currentSellValue = 0;
            int buyDay = 0;
            int saleDay = 0;
            int tempPurchaseDay = 0;
            for (int day = 1; day < days; day++) {
                //If we find a Better profit on some day that is better than the previous day, update the currentSellValue to reflect 
                //That profit and also update the transaction details. 
                currentSellValue = Math.max(previousSellValue, previousBuyValue + stocks[i][day]);
                if (currentSellValue > previousSellValue) {
                    buyDay = tempPurchaseDay;
                    saleDay = day;
                }
                //If we find a buyDay, that is less price than our previous Buy day for some Stock i, update our currentBuyday,
                //Also update previousBuy and sell Values since we are ending this current iteration.
                currentBuyValue = Math.max(previousBuyValue, -stocks[i][day]);
                if (currentBuyValue > previousBuyValue)
                    tempPurchaseDay = day;
                previousBuyValue = currentBuyValue;
                previousSellValue = currentSellValue;
            }
            //Update the global maximumProfit if we find a better profit and also update the transaction Details.
            if (previousSellValue > maxProfit) {
                maxProfit = previousSellValue;
                txn = new Transaction(i, buyDay, saleDay, stocks[i][saleDay] - stocks[i][buyDay]);
            }
        }
        return txn;
    }
}
