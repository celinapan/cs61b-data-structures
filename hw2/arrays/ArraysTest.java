package arrays;

import org.junit.Test;
import static org.junit.Assert.*;
/** Testing the Arrays.java
 *  @author Trang Van
 */

public class ArraysTest {
    /** Testing Array Class methods: catenate, remove, naturalRuns
     *
     */
    int[] A = {1, 2, 4, 6, 8};
    int[] B = {10, 12, 14};
    int[] C = {1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
    int[] D = {1};
    @Test
    public void testCatenate() {
        int[] expectedAB = {1, 2, 4, 6, 8, 10, 12, 14};
        assertArrayEquals(expectedAB, Arrays.catenate(A, B));
    }
    @Test
    public void testRemove() {
        int[] expectedCRemove = {1, 3, 7, 10, 11};
        int[] expectedD = {};
        assertArrayEquals(expectedCRemove, Arrays.remove(C, 3, 5));
        assertArrayEquals(expectedD, Arrays.remove(D, 0, 1));
    }
    @Test
    public void testNaturalRunsArr() {
        int[][] expectedC = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        assertArrayEquals(expectedC, Arrays.naturalRuns(C));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
