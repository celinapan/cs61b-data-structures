package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** Testing Seam Carving with MatrixUtils.java
 *  @author Trang Van
 */

public class MatrixUtilsTest {
    /** Test accumulateVertical and accumulate.
     *  accumulate specifies the orientation VERTICAL or HORIZONTAL
     *      HORIZONTAL will call on the transpose helper function
     *      that reorients the matrix before applying accumulate vertical.
     */


    double[][] A = new double[][] { {1000000, 1000000, 1000000, 1000000},
                                    {1000000, 75990, 30003, 1000000},
                                    {1000000, 30002, 103046, 1000000},
                                    {1000000, 29515, 38273, 1000000},
                                    {1000000, 73403, 35399, 1000000},
                                    {1000000, 1000000, 1000000, 1000000}};
    double [][] horizontalA = new double[][] {{1000000, 1000000, 1000000, 1000000, 1000000, 1000000},
            {1000000, 75990, 30002, 29515, 73403, 1000000},
            {1000000, 30003, 103046, 38273, 35399, 1000000},
            {1000000, 1000000, 1000000, 1000000, 1000000, 1000000}};

    double[][] expectedA = new double[][] { {1000000 ,1000000, 1000000,1000000},
                                            {2000000, 1075990, 1030003, 2000000},
                                            {2075990, 1060005,   1133049,   2030003},
                                            {2060005, 1089520,   1098278,   2133049},
                                            {2089520, 1162923,   1124919,   2098278},
                                            {2162923, 2124919,   2124919,   2124919}};
    @Test
    public void testAccumulateVertical () {
        assertArrayEquals(expectedA, MatrixUtils.accumulateVertical(A));
    }

    @Test
    public void testAccumulate () {
        assertArrayEquals(MatrixUtils.transpose(expectedA), MatrixUtils.accumulate(horizontalA, MatrixUtils.Orientation.HORIZONTAL));

    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
