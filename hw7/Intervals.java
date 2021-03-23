import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/** HW #7, Sorting ranges.
 *  @author
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        // REPLACE WITH APPROPRIATE STATEMENTS.
        // intervals.get(0) => {19, 30}
        // intervals.get(0)[0] = 19 = start & intervals.get(0)[1] = 30 = end

        //Sorting idea from Piazza - IntelliJ suggested Comparator
        intervals.sort(Comparator.comparingInt(l -> l[0]));
        int min = Integer.MIN_VALUE;
        int max = Integer.MIN_VALUE;
        int length = 0;

        for (int i = 0; i < intervals.size(); i += 1) {
            int[] bound = intervals.get(i);
            if (bound[0] > max) {
                length += (max - min);
                min = bound[0];
                max = bound[1];
            } else if (bound[1] > max) {
                max = bound[1];
            }
        }

        length += max - min;
        return length;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
