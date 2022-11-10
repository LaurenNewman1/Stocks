import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JUnitTests {

    void runAllProblem1(int input[][], Transaction expected) {
        assertTransaction(expected, Problem1.bruteForce(input));
        assertTransaction(expected, Problem1.greedy(input));
        assertTransaction(expected, Problem1.dynamicBU(input));
        assertTransaction(expected, Problem1.dynamicMem(input));
    }

    void assertTransaction(Transaction a, Transaction b) {
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

    void evaluate_prob1(int m[], int n[]) {
        long bruteTime[] = new long[5];
        long greedyTime[] = new long[5];
        long dynamicMemTime[] = new long[5];
        long dynamicBUTime[] = new long[5];
        Random rand = new Random();
        for (int k = 0; k < 5; k++) {
            // Setup randomized stocks
            int[][] stocks = new int[m[k]][n[k]];
            for (int i = 0; i < m[k]; i++) {
                for (int j = 0; j < n[k]; j++) {
                    stocks[i][j] = rand.nextInt(100) + 1;
                }
            }
            // Brute Force
            long start = System.nanoTime();
            Problem1.bruteForce(stocks);
            long finish = System.nanoTime();
            bruteTime[k] = finish - start;
            // Greedy
            start = System.nanoTime();
            Problem1.greedy(stocks);
            finish = System.nanoTime();
            greedyTime[k] = finish - start;
            // Dynamic Memoization
            start = System.nanoTime();
            Problem1.dynamicMem(stocks);
            finish = System.nanoTime();
            dynamicMemTime[k] = finish - start;
            // Dynamic Bottom Up
            start = System.nanoTime();
            Problem1.dynamicBU(stocks);
            finish = System.nanoTime();
            dynamicBUTime[k] = finish - start;
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
        int n[] = {10, 10, 10, 10, 10};
        int m[] = {100, 200, 300, 400, 500};
        evaluate_prob1(m, n);
    }

}
