/*
This function is the main entry point into our entire program. It functions as specified in the requirement document.
Handles running all of the algorithms based on the User's input/
*/
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        // Which task to run (Given by the User as <Stocks task>)?
        Scanner scan = new Scanner(System.in);
        String task = args[0];

        // Checking whether the user selects the tasks of Problem 1.
        if (task.equals("1") || task.equals("2") || task.equals("3a") || task.equals("3b")){
            // Take in the input and save in stocks
            int m = scan.nextInt();
            int n = scan.nextInt();
            int stocks[][] = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    stocks[i][j] = scan.nextInt();
                }
            }
            // Execute all Problem 1 tasks based on User Input.
            Transaction optimal = new Transaction();
            switch (task) {
                case "1":
                    optimal = Problem1.bruteForce(stocks);
                    break;
                case "2":
                    optimal = Problem1.greedy(stocks);
                    break;
                case "3a":
                    optimal = Problem1.dynamicMem(stocks);
                    break;
                case "3b":
                    optimal = Problem1.dynamicBU(stocks);
                    break;
            }
            // Results
            optimal.print();
        }
        // Checking whether the user selects the tasks of Problem 2.
        else if (task.equals("4") || task.equals("5") || task.equals("6a") || task.equals("6b")){
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
            // Execute all Problem 2 tasks based on User Input.
            Transaction[] optimal = new Transaction[k];
            switch (task) {
                case "4":
                    optimal = Problem2.bruteForce(stocks, k);
                    break;
                case "5":
                    optimal = Problem2.dynamic1(stocks, k);
                    break;
                case "6a":
                    optimal = Problem2.dynamic2Mem(stocks, k);
                    break;
                case "6b":
                    optimal = Problem2.dynamic2BU(stocks, k);
                    break;
            }
            // Results
            for (int i = 0; i < k; i++) {
                if (optimal[i] != null) {
                    optimal[i].print();
                }
            }
        }
        // Checking whether the user selects the tasks of Problem 3.
        else if (task.equals("7") || task.equals("8") || task.equals("9a") || task.equals("9b")){
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
                // Execute all Problem 3 tasks based on User Input.
                List<Transaction> optimal = new ArrayList<>();
                switch (task) {
                    case "7":
                        optimal = Problem3.bruteForce(stocks, c);
                        break;
                    case "8":
                        optimal = Problem3.dynamic1(stocks, c);
                        break;
                    case "9a":
                        optimal = Problem3.dynamic2Mem(stocks, c);
                        break;
                    case "9b":
                        optimal = Problem3.dynamic2BU(stocks, c);
                        break;
                }
                // Results
                for (Transaction t : optimal) {
                    if (t != null) {
                        t.print();
                    }
                }
        }
        //Handling the Invalid Input Case. 
        else {
            System.out.println("Invalid input. Try again with a task 1-9b");
        }
    }
}
