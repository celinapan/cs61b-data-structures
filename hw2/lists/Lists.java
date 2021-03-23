package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

import com.sun.xml.internal.xsom.XSUnionSimpleType;

/** List problem.
 *  @author
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        IntListList result = new IntListList();
        if (L == null) {
            return result;
        } else if (L.tail == null) { // one element
            result.head = L;
        } else {
            IntListList next;
            IntList grouper, iterator;

            next = result;
            result.head = L;
            grouper = L;
            iterator = L.tail;
            while (iterator != null) {
                if (iterator.head <= grouper.head) {
                    IntList cutOff = grouper;
                    next.tail = new IntListList();
                    next.tail.head = iterator;

                    iterator = iterator.tail;
                    grouper = grouper.tail;
                    cutOff.tail = null;
                    next = next.tail;
                } else {
                    iterator = iterator.tail;
                    grouper = grouper.tail;
                }
            }
        }

        return result;
    }
}
