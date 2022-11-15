import java.util.ArrayList;
import java.util.List;

public class Problem3 {

    public static List<Transaction> bruteForce(int[][] stocks, int c) {
        List<Transaction> opt = bruteRecur(stocks, 0, c, 0, new ArrayList<>(), new ArrayList<>());
        return opt;
    }
    private static List<Transaction> bruteRecur(int[][] stocks, int start, int c, int soFar, List<Transaction> txns, List<Transaction> finTxns) {
        int m = stocks.length;
        int n = stocks[0].length;
        if (start == n - 1) {
            if (soFar > Transaction.getProfit(finTxns)) {
                finTxns = new ArrayList<>(txns);
            }
            return finTxns;
        }
        for (int j1 = start; j1 < n; j1++) {
            for (int j2 = j1 + 1; j2 < n; j2++) {
                Transaction maxTrans = new Transaction();
                for (int i = 0; i < m; i++) {
                    if (stocks[i][j2] - stocks[i][j1] > maxTrans.profit) {
                        maxTrans = new Transaction(i, j1, j2, stocks[i][j2] - stocks[i][j1]);
                    }
                }
                txns.add(maxTrans);
                finTxns = bruteRecur(stocks, j2 + c, c, soFar + maxTrans.profit, txns, finTxns);
            }
        }
        return finTxns;
    }
    public static List<Transaction> dynamic1(int[][] stocks, int c) {
        throw new UnsupportedOperationException();
    }

    public static List<Transaction> dynamic2Mem(int[][] stocks, int c) {
        throw new UnsupportedOperationException();
    }

    public static List<Transaction> dynamic2BU(int[][] stocks, int c) {
        throw new UnsupportedOperationException();
    }
}
