import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JUnitTests {

    void runAllProblem1(int input[][], Transaction expected) {
        assertTransaction(expected, Problem1.bruteForce(input));
        assertTransaction(expected, Problem1.greedy(input));
        assertTransaction(expected, Problem1.dynamicBU(input));
        assertTransaction(expected, Problem1.dynamicMem(input));
    }

    void runAllProblem2(int input[][], int k, Transaction[] expected) {
        Transaction[] actual = Problem2.bruteForce(input, k);
        for (int i = 0; i < actual.length; i++)
            assertTransaction(expected[i], actual[i]);
        actual = Problem2.dynamic1(input, k);
        for (int i = 0; i < actual.length; i++)
            assertTransaction(expected[i], actual[i]);
        actual = Problem2.dynamic2BU(input, k);
        for (int i = 0; i < actual.length; i++)
            assertTransaction(expected[i], actual[i]);
        actual = Problem2.dynamic2Mem(input, k);
        for (int i = 0; i < actual.length; i++)
            assertTransaction(expected[i], actual[i]);
    }

    void runAllProblem3(int input[][], int c, List<Transaction> expected) {
//        List<Transaction> actual = Problem3.bruteForce(input, c);
//        for (int i = 0; i < actual.size(); i++)
//            assertTransaction(expected.get(i), actual.get(i));
        List<Transaction> actual = Problem3.dynamic1(input, c);
        for (int i = 0; i < actual.size(); i++)
            assertTransaction(expected.get(i), actual.get(i));
//        actual = Problem3.dynamic2BU(input, c);
//        for (int i = 0; i < actual.size(); i++)
//            assertTransaction(expected.get(i), actual.get(i));
//        actual = Problem3.dynamic2Mem(input, c);
//        for (int i = 0; i < actual.size(); i++)
//            assertTransaction(expected.get(i), actual.get(i));
    }

    void assertTransaction(Transaction a, Transaction b) {
        System.out.print("Expected: ");
        a.print();
        System.out.print("Actual: ");
        b.print();
        assertEquals(a.stock, b.stock);
        assertEquals(a.buyDay, b.buyDay);
        assertEquals(a.sellDay, b.sellDay);
        assertEquals(a.profit, b.profit);
    }

    @Test
    void p1_test1() {
        int stocks[][] = {
                { 1, 8, 9 },
                { 1, 2, 3}
        };
        Transaction expected = new Transaction(0, 0, 2, 8);
        runAllProblem1(stocks, expected);
    }

    @Test
    void p1_test2() {
        int stocks[][] = {
                { 5, 2, 6, 10, 2, 8, 7, 9 },
                { 2, 1, 5, 3, 9, 9, 5, 8},
                { 1, 2, 3, 4, 3, 2, 5, 1}
        };
        Transaction expected = new Transaction(0, 1, 3, 8);
        runAllProblem1(stocks, expected);
    }

    @Test
    void p2_test1() {
        int k = 1;
        int stocks[][] = {
                { 1, 8, 9 },
                { 1, 2, 3}
        };
        Transaction[] expected = {
                new Transaction(0, 0, 2, 8)
        };
        runAllProblem2(stocks, k, expected);
    }

    @Test
    void p2_test2() {
        int k = 3;
        int stocks[][] = {
                { 5, 2, 6, 10, 2, 8, 7, 9 },
                { 2, 1, 5, 3, 9, 9, 5, 8},
                { 1, 2, 3, 4, 3, 2, 5, 1}
        };
        Transaction[] expected = {
                new Transaction(0, 4, 7, 7),
                new Transaction(1, 3, 4,6),
                new Transaction(0, 1, 3, 8)
        };
        runAllProblem2(stocks, k, expected);
    }

    @Test
    void p2_test3() {
        int k = 2;
        int stocks[][] = {
                { 1, 5, 1},
                { 1, 1, 6}
        };
        Transaction[] expected = {
                new Transaction(1, 1, 2,5),
                new Transaction(0, 0, 1, 4)
        };
        runAllProblem2(stocks, k, expected);
    }

    @Test
    void p3_test2() {
        int c = 0;
        int stocks[][] = {
                { 5, 2, 6, 10, 2},
                { 2, 1, 5, 3, 9},
                { 1, 2, 3, 4, 3}
        };
        List<Transaction> expected = new ArrayList<>();
        expected.add(new Transaction(2, 0, 1, 1));
        expected.add(new Transaction(0, 1, 2,4));
        expected.add(new Transaction(0, 2, 3, 4));
        expected.add(new Transaction(1, 3, 4, 7));
        runAllProblem3(stocks, c, expected);
    }

    @Test
    void p3_test3() {
        int c = 0;
        int stocks[][] = {
                { 1, 5, 1},
                { 1, 1, 6}
        };
        List<Transaction> expected = new ArrayList<>();
        expected.add(new Transaction(0, 0, 1, 4));
        expected.add(new Transaction(1, 1, 2,5));
        runAllProblem3(stocks, c, expected);
    }

    @Test
    void p3_test4() {
        int c = 2;
        int stocks[][] = {
                {5, 2, 6, 10, 2, 8, 7, 9},
                {2, 1, 5, 3, 10, 9, 5, 8},
                {1, 2, 3, 4, 3, 2, 5, 1}
        };
        List<Transaction> expected = new ArrayList<>();
        expected.add(new Transaction(1, 6, 7,3));
        expected.add(new Transaction(1, 1, 4, 9));
        runAllProblem3(stocks, c, expected);
    }

    int[][] getRandomStocks(int m, int n) {
        int[][] stocks = new int[m][n];
        Random rand = new Random();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                stocks[i][j] = rand.nextInt(100) + 1;
            }
        }
        return stocks;
    }

    void evaluate_prob1(int m[], int n[]) {
        long bruteTime[] = new long[5];
        long greedyTime[] = new long[5];
        long dynamicMemTime[] = new long[5];
        long dynamicBUTime[] = new long[5];
        for (int i = 0; i < 5; i++) {
            // Setup randomized stocks
            int[][] stocks = getRandomStocks(m[i], n[i]);
            // Brute Force
            long start = System.nanoTime();
            Problem1.bruteForce(stocks);
            long finish = System.nanoTime();
            bruteTime[i] = finish - start;
            // Greedy
            start = System.nanoTime();
            Problem1.greedy(stocks);
            finish = System.nanoTime();
            greedyTime[i] = finish - start;
            // Dynamic Memoization
            start = System.nanoTime();
            Problem1.dynamicMem(stocks);
            finish = System.nanoTime();
            dynamicMemTime[i] = finish - start;
            // Dynamic Bottom Up
            start = System.nanoTime();
            Problem1.dynamicBU(stocks);
            finish = System.nanoTime();
            dynamicBUTime[i] = finish - start;
        }
        System.out.println("Brute force times: ");
        for (long time : bruteTime)
            System.out.print(time + " ");
        System.out.println("\nGreedy times: ");
        for (long time : greedyTime)
            System.out.print(time + " ");
        System.out.println("\nDynamic Mem times: ");
        for (long time : dynamicMemTime)
            System.out.print(time + " ");
        System.out.println("\nDynamic BU times: ");
        for (long time : dynamicBUTime)
            System.out.print(time + " ");
    }

    @Test
    void p1_evaluation_plot1() {
        int m[] = {10, 10, 10, 10, 10};
        int n[] = {100, 200, 300, 400, 500};
        evaluate_prob1(m, n);
    }

    @Test
    void p1_evaluation_plot2() {
        int m[] = {100, 200, 300, 400, 500};
        int n[] = {10, 10, 10, 10, 10};
        evaluate_prob1(m, n);
    }

    void evaluate_prob2(int m[], int n[], int k[]) {
        long bruteTime[] = new long[5];
        long dynamic1Time[] = new long[5];
        long dynamic2MemTime[] = new long[5];
        long dynamic2BUTime[] = new long[5];
        for (int i = 0; i < 5; i++) {
            // Setup randomized stocks
            int[][] stocks = getRandomStocks(m[i], n[i]);
            // Brute Force
            long start = System.nanoTime();
            Problem2.bruteForce(stocks, k[i]);
            long finish = System.nanoTime();
            bruteTime[i] = finish - start;
            // Dynamic 1
            start = System.nanoTime();
            Problem2.dynamic1(stocks, k[i]);
            finish = System.nanoTime();
            dynamic1Time[i] = finish - start;
            // Dynamic 2 Memoization
            start = System.nanoTime();
            Problem2.dynamic2Mem(stocks, k[i]);
            finish = System.nanoTime();
            dynamic2MemTime[i] = finish - start;
            // Dynamic 2 Bottom Up
            start = System.nanoTime();
            Problem2.dynamic2BU(stocks, k[i]);
            finish = System.nanoTime();
            dynamic2BUTime[i] = finish - start;
        }
        System.out.println("Brute force times: ");
        for (long time : bruteTime)
            System.out.print(time + " ");
        System.out.println("\nDynamic 1 times: ");
        for (long time : dynamic1Time)
            System.out.print(time + " ");
        System.out.println("\nDynamic 2 Mem times: ");
        for (long time : dynamic2MemTime)
            System.out.print(time + " ");
        System.out.println("\nDynamic 2 BU times: ");
        for (long time : dynamic2BUTime)
            System.out.print(time + " ");
    }

    @Test
    void p2_evaluation_plot1() {
        int m[] = {5, 5, 5, 5, 5};
        int n[] = {5, 10, 15, 20, 25};
        int k[] = {5, 5, 5, 5, 5};
        evaluate_prob2(m, n, k);
    }

    @Test
    void p2_evaluation_plot2() {
        int m[] = {100, 200, 300, 400, 500};
        int n[] = {10, 10, 10, 10, 10};
        int k[] = {5, 5, 5, 5, 5};
        evaluate_prob2(m, n, k);
    }

    @Test
    void p2_evaluation_plot3() {
        int m[] = {10, 10, 10, 10, 10};
        int n[] = {15, 15, 15, 15, 15};
        int k[] = {2, 4, 6, 8, 10};
        evaluate_prob2(m, n, k);
    }

    void evaluate_prob3(int m[], int n[], int c[]) {
        long bruteTime[] = new long[5];
        long dynamic1Time[] = new long[5];
        long dynamic2MemTime[] = new long[5];
        long dynamic2BUTime[] = new long[5];
        for (int i = 0; i < 5; i++) {
            // Setup randomized stocks
            int[][] stocks = getRandomStocks(m[i], n[i]);
            // Brute Force
            long start = System.nanoTime();
            Problem3.bruteForce(stocks, c[i]);
            long finish = System.nanoTime();
            bruteTime[i] = finish - start;
            // Dynamic 1
            start = System.nanoTime();
            Problem3.dynamic1(stocks, c[i]);
            finish = System.nanoTime();
            dynamic1Time[i] = finish - start;
            // Dynamic 2 Memoization
            start = System.nanoTime();
            Problem3.dynamic2Mem(stocks, c[i]);
            finish = System.nanoTime();
            dynamic2MemTime[i] = finish - start;
            // Dynamic 2 Bottom Up
            start = System.nanoTime();
            Problem3.dynamic2BU(stocks, c[i]);
            finish = System.nanoTime();
            dynamic2BUTime[i] = finish - start;
        }
        System.out.println("Brute force times: ");
        for (long time : bruteTime)
            System.out.print(time + " ");
        System.out.println("\nDynamic 1 times: ");
        for (long time : dynamic1Time)
            System.out.print(time + " ");
        System.out.println("\nDynamic 2 Mem times: ");
        for (long time : dynamic2MemTime)
            System.out.print(time + " ");
        System.out.println("\nDynamic 2 BU times: ");
        for (long time : dynamic2BUTime)
            System.out.print(time + " ");
    }

    @Test
    void p3_evaluation_plot1() {
        int m[] = {10, 10, 10, 10, 10};
        int n[] = {100, 200, 300, 400, 500};
        int c[] = {10, 10, 10, 10, 10};
        evaluate_prob3(m, n, c);
    }

    @Test
    void p3_evaluation_plot2() {
        int m[] = {100, 200, 300, 400, 500};
        int n[] = {10, 10, 10, 10, 10};
        int c[] = {10, 10, 10, 10, 10};
        evaluate_prob3(m, n, c);
    }

}
