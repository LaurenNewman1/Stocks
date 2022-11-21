/* 
This class is used to create a new Data Structure to cache our intermediate results and track the minimum Cost, 
Profit and the transactions that are linked to it. 
@param: 
m(required) - Number of stocks 
n(required) - Number of days 
@returns - A new Object of Type Cache.
*/

public class Cache {
    int[][] minCost;
    int[][] maxProfit;
    Transaction[][] dpTxn;
    int m;
    int n;
    //Initialize the 2D arrays to keep track of the minimum Cost, the maximumProfit for each stock upto that day and 
    //another 2D array to track the optimal transactions.
    Cache(int m, int n) {
        minCost = new int[m][n];
        maxProfit = new int[m][n];
        dpTxn = new Transaction[m][n];
        this.m = m;
        this.n = n;
    }

    // This is a debug helper function (**Not part of the code**)
    public void print() {
        System.out.println("Cost Table");
        for (int i = 0; i < m; i++) {
            System.out.println();
            for (int j = 0; j < n; j++) {
                System.out.print(minCost[i][j] + " ");
            }
        }
        System.out.println("\n\nProfit Table");
        for (int i = 0; i < m; i++) {
            System.out.println();
            for (int j = 0; j < n; j++) {
                System.out.print(maxProfit[i][j] + " ");
            }
        }
        System.out.println("\n");
    }
}
