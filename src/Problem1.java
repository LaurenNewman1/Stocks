public class Problem1 {

    public static Transaction bruteForce(int[][] stocks) {
        int m = stocks.length;
        int n = stocks[0].length;
        Transaction opt = new Transaction(0, 0, 0, 0);
        for (int i = 0; i < m; i++) {
            for (int j1 = 0; j1 < n; j1++) {
                for (int j2 = j1 + 1; j2 < n; j2++) {
                    if (stocks[i][j2] - stocks[i][j1] > opt.profit) {
                        opt = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1]);
                    }
                }
            }
        }
        return opt;
    }

    public static Transaction greedy(int[][] stocks) {
        throw new UnsupportedOperationException();
    }

    public static Transaction dynamicMem(int[][] stocks) {
        throw new UnsupportedOperationException();
    }

    public static Transaction dynamicBU(int[][] stocks) {
        throw new UnsupportedOperationException();
    }
}
