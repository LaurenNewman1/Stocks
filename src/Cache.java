public class Cache {
    int[][] minCost;
    int[][] maxProfit;
    Transaction[][] dpTxn;
    int m;
    int n;

    Cache(int m, int n) {
        minCost = new int[m][n];
        maxProfit = new int[m][n];
        dpTxn = new Transaction[m][n];
        this.m = m;
        this.n = n;
    }

    // Debug helper function
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
