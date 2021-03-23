package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int [] newArr = new int[A.length + B.length];
        int nextIdx = 0;
        for (int i = 0; i < A.length; i += 1) {
            newArr[i] = A[i];
            nextIdx = i;
        }
        for (int i = 0; i < B.length; i += 1) {
            newArr[nextIdx + 1] = B[i];
            nextIdx += 1;
        }
        return newArr;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        if (len > A.length || start > A.length - 1) {
            return new int [] {};
        }
        int[] newArr = new int [A.length - len];
        int nextIdx = 0;
        for(int i = 0; i < start; i += 1) {
            newArr[i] = A[i];
            nextIdx = i;
        }
        for (int j = start + len; j < A.length; j += 1) {
            newArr[nextIdx + 1] = A[j];
            nextIdx += 1;
        }
        return newArr;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int[][] result;
        if (A.length == 0) {
            result = new int[][] {};
        } else if (A.length == 1) {
            result = new int[][] {{A[0]}};
        }

        int numGroups = 1;
        int len = 0, j = 0;
        int[] lenList = new int[A.length];
        for(int i = 0; i < A.length-1; i++){
            len += 1;
            if (A[i] >= A[i + 1]){
                numGroups += 1;
                lenList[j] = len;
                j += 1;
                len = 0;
            }
            if (i == A.length - 2){
                lenList[j] = len + 1;
            }

        }

        result = new int[numGroups][];
        int lenListIdx = 0;
        int[] group = new int[lenList[lenListIdx]];
        int currGroup = 0;      // for result[0]
        int iter = 0;
        int prev = A[0];
        for(int i = 1; i <= A.length; i += 1) {
            if (i == A.length){
                group[iter] = prev;
                result[currGroup] = group;
            } else if (A[i] <= prev) {
                group[iter] = prev;
                result[currGroup] = group;
                currGroup += 1;
                lenListIdx += 1;
                group = new int [lenList[lenListIdx]];
                iter = 0;   // reset for group
                prev = A[i];
            } else {
                group[iter] = prev;
                prev = A[i];
                iter += 1;
            }
        }
        return result;
    }
}
