import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author YOUR NAMES HERE
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(14, Utils.max(new int[] { 0, -5, 2, 14, 10 }));
        assertEquals(55, Utils.max(new int [] {55, 2, 3, -7, 1, -2, 40, 55}));
        assertEquals(9, Utils.max(new int [] {-8, 9, 0}));
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(Utils.threeSum(new int[] { -6, 3, 10, 200}));
        assertTrue(Utils.threeSum(new int[]{8, 2, -1, 15}));
        assertFalse(Utils.threeSum(new int[]{-6, 2, 5}));
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertFalse(Utils.threeSumDistinct(new int[] { -6, 3, 10, 200 }));
        assertFalse(Utils.threeSumDistinct(new int[]{8, 2, -1, 15}));
        assertTrue(Utils.threeSumDistinct(new int[]{8, 2, -1, -1, 15}));
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
