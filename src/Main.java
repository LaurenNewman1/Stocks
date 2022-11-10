import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        // Which task to run?
        Scanner scan = new Scanner(System.in);
        int task = 0;

        System.out.println("Which task would you like to run?");
        System.out.println("(1) Problem 1 - Brute Force Θ(m ∗ n2) ");
        System.out.println("(2) Problem 1 - Greedy Θ(m ∗ n)");
        System.out.println("(3) Problem 1 - Dynamic (Memoization) Θ(m ∗ n)");
        System.out.println("(4) Problem 1 - Dynamic (Bottom Up) Θ(m ∗ n)");
        System.out.println("(5) Problem 2 - Brute Force Θ(m ∗ n2k)");
        System.out.println("(6) Problem 2 - Dynamic 1 Θ(m ∗ n2 ∗ k)");
        System.out.println("(7) Problem 2 - Dynamic 2 (Memoization) Θ(m ∗ n ∗ k)");
        System.out.println("(8) Problem 2 - Dynamic 2 (Bottom Up) Θ(m ∗ n ∗ k)");
        System.out.println("(9) Problem 3 - Brute Force Θ(m ∗ 2n)");
        System.out.println("(10) Problem 3 - Dynamic 1 Θ(m ∗ n2)");
        System.out.println("(11) Problem 3 - Dynamic 2 (Memoization) Θ(m ∗ n)");
        System.out.println("(12) Problem 3 - Dynamic 2 (Bottom Up) Θ(m ∗ n)");
        // Error handling
        while (task < 1 || task > 12) {
            task = scan.nextInt();
            if (task < 1 || task > 8) {
                System.out.println("Please enter a valid selection 1-8.");
            }
        }
        // Problem 1
        if (task <= 4){
            System.out.println("Task " + task + ": Please enter data in the following format.");
            System.out.println("\tLine 1: two integers m and n separated by a space");
            System.out.println("\tLines 2...m+1: n integer prices separated by a space");
            // Take in the input and save in stocks
            int m = scan.nextInt(); // TODO I assume we don't need error handling to check input format?
            int n = scan.nextInt();
            int stocks[][] = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    stocks[i][j] = scan.nextInt();
                }
            }
            // Execute
            System.out.println("Executing task (" + task + ") ...");
            Transaction optimal = new Transaction();
            switch (task) {
                case 1 -> optimal = Problem1.bruteForce(stocks);
                case 2 -> optimal = Problem1.greedy(stocks);
                case 3 -> optimal = Problem1.dynamicMem(stocks);
                case 4 -> optimal = Problem1.dynamicBU(stocks);
            }
            // Results
            System.out.println("Optimal Transaction:");
            System.out.print("\t");
            optimal.print();
        }
        // Problem 2
        else if (task >= 5 && task <= 8) {
            System.out.println("Task " + task + ": Please enter data in the following format.");
            System.out.println("\tLine 1: one integer k");
            System.out.println("\tLine 2: two integers m and n separated by a space");
            System.out.println("\tLines 3...m+2: n integer prices separated by a space");
            // Take in the input and save in stocks
            int k = scan.nextInt();
            int m = scan.nextInt();
            int n = scan.nextInt();
            int stocks[][] = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    stocks[i][j] = scan.nextInt();
                }
            }
            // Execute
            System.out.println("Executing task (" + task + ") ...");
            Transaction[] optimal = new Transaction[k];
            switch (task) {
                case 5 -> Problem2.bruteForce(stocks, k);
                case 6 -> Problem2.dynamic1(stocks, k);
                case 7 -> Problem2.dynamic2Mem(stocks, k);
                case 8 -> Problem2.dynamic2BU(stocks, k);
            }
            // Results
            System.out.println("Optimal List of Transactions:");
            for (int i = 0; i < k; i++) {
                if (optimal[i].stock > 0) {
                    System.out.print("\t");
                    optimal[i].print();
                }
            }
        }
        // Problem 3
        else {
                System.out.println("Task " + task + ": Please enter data in the following format.");
                System.out.println("\tLine 1: one integer c");
                System.out.println("\tLine 2: two integers m and n separated by a space");
                System.out.println("\tLines 3...m+2: n integer prices separated by a space");
                // Take in the input and save in stocks
                int c = scan.nextInt();
                int m = scan.nextInt();
                int n = scan.nextInt();
                int stocks[][] = new int[m][n];
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        stocks[i][j] = scan.nextInt();
                    }
                }
                // Execute
                System.out.println("Executing task (" + task + ") ...");
                List<Transaction> optimal = new ArrayList<>();
                switch (task) {
                    case 9 -> Problem3.bruteForce(stocks, c);
                    case 10 -> Problem3.dynamic1(stocks, c);
                    case 11 -> Problem3.dynamic2Mem(stocks, c);
                    case 12 -> Problem3.dynamic2BU(stocks, c);
                }
                // Results
                System.out.println("Optimal List of Transactions:");
                for (Transaction t : optimal) {
                    if (t.stock > 0) {
                        System.out.print("\t");
                        t.print();
                    }
                }
        }
    }
}
