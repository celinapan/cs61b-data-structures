package lists;

import org.junit.Assert;
import org.junit.Test;


/** Testing Lists.naturalRuns
 *
 *  @author Trang Van
 */

public class ListsTest {
    /** Test naturalRuns with an empty IntList, one-element IntList, and IntList with 2+ elements.
     * + Used IntListList.equal() to check if contents of expected and return value of naturalRuns is the same.
     * + assertTrue if the contents match.
     */

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.
    @Test
    public void testNaturalRuns() {
        int [][] expA = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        int [][] expB = {{1}};
        int [][] expC = {{}};
        IntList L = IntList.list(new int [] {1, 3, 7, 5, 4, 6, 9, 10, 10, 11});
        IntList L2 = IntList.list(new int [] {1});
        IntList L3 = IntList.list(new int[] {});

        IntListList expected1 = IntListList.list(expA);
        IntListList expected2 = IntListList.list(expB);
        IntListList expected3 = IntListList.list(expC);

        Assert.assertTrue(expected1.equals(Lists.naturalRuns(L)));
        Assert.assertTrue(expected2.equals(Lists.naturalRuns(L2)));
        Assert.assertTrue(expected3.equals(Lists.naturalRuns(L3)));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
