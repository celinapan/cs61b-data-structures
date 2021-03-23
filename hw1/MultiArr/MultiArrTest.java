import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int [][] A = { {1, 2, 3, 4, 5},
                       {0, 2, 4, 6, 8},
                       {5, 10, 15, 5, 10}};
        int [][] A2 = {{0, 0, 0}};
        assertEquals(15, MultiArr.maxValue(A));
        assertEquals(0, MultiArr.maxValue(A2));
    }

    @Test
    public void testAllRowSums() {
        int [][] A = {{1, 2, 3, 4, 5},
                      {0, 2, 4, 6, 8},
                      {5, 10, 15, 5, 10}};
        int [][] A2 = {{0, 0, 1}};
        int [] expectedA = {15, 20, 45};
        int [] expectedA2 = {1};
        assertArrayEquals(expectedA, MultiArr.allRowSums(A));
        assertArrayEquals(expectedA2 , MultiArr.allRowSums(A2));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
