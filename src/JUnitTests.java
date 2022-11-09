import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JUnitTests {

    void runAllProblem1(int input[][], Transaction expected) {
        assertTransaction(expected, Problem1.bruteForce(input));
        assertTransaction(expected, Problem1.greedy(input));
        assertTransaction(expected, Problem1.dynamicMem(input));
        assertTransaction(expected, Problem1.dynamicBU(input));
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

}
