import java.util.*;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            int key, temp;
            // Element 0 is already sorted
            for (int i = 1; i < k; i++) {
                 key = array[i];
                 for (int j = i - 1; j >= 0; j --) {
                     if (key < array[j]) {
                         temp = array[j];
                         array[j] = array[j + 1];
                         array[j + 1] = temp;
                     }
                 }


            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            assert(k <= array.length);
            int minValue, minIndex, temp;
            for (int i = 0; i < k; i ++) {
                minValue = array[i];
                minIndex = i;
                for (int j = i; j < k; j++) {
                    if (array[j] < minValue) {
                        minValue = array[j];
                        minIndex = j;
                    }
                }
                if (minValue < array[i]) {
                    temp = array[i];
                    array[i] = array[minIndex];
                    array[minIndex] = temp;
                }
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
     * Source (implement without low, high param): Baeldung.com
     * Source inspo - merge: howtodoinjava.com
     */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            if (k == 1) {
                return;
            }

            int midIndex = k/2;
            // Split Array until mid index
            int[] left = new int[midIndex];         //the first mid elems
            int[] right = new int[k - midIndex];    //remaning elems (end index - mid)
            System.arraycopy(array, 0, left, 0, midIndex);
            System.arraycopy(array, midIndex, right, 0, k-midIndex);

            sort(left, midIndex);
            sort(right, k - midIndex);

            merge(array, left, right, midIndex, k - midIndex);
        }

        // may want to add additional methods
        public void merge(int[] array, int[] left, int[] right, int l, int r) {
            int i = 0, j = 0, k = 0;
            while (i < l && j < r) {
                if(left[i] < right[j]) {
                    array[k] = left[i];
                    i += 1;
                } else {
                    array[k] = right[j];
                    j += 1;
                }
                k += 1;
            }
            System.arraycopy(left, i, array, k, left.length - i);
            System.arraycopy(right, j, array, k, right.length - j);

            /*
            // Test Prints
            System.out.println();

            System.out.println("Left Idx: " + l +" Right Idx: " + r);
            System.out.println("Array:");
            for (int x: array) {
                System.out.print(x + " ");;
            }
            System.out.println();
            System.out.println("Left:");
            for (int x: left) {
                System.out.print(x + " ");;
            }
            System.out.println();
            System.out.println("Right:");

            for (int x: left) {
                System.out.print(x + " ");;
            }

             */
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     * Source: Geeksforgeeks method (not using bits)
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override

        public void sort(int[] a, int k) {
            // FIXME

            // Find max val to determine count length
            int maxVal = 0;
            for (int i = 0; i < a.length; i++) {
                if (a[i] > maxVal) {
                    maxVal = a[i];
                }
            }
            // Initialize Counts to tally frequency
            for (int digit = 1; maxVal /digit > 0; digit *= 10) {
                int[] count = new int[10];
                int i;
                // Get frequency at each bucket
                for (i = 0; i < k; i++) {
                    int lsdBucket = (a[i]/digit) % 10;
                    count[lsdBucket] += 1;
                }
                // Shift count position to digit's position
                for(i = 1; i < 10; i ++) {
                    count[i] += count[i-1];
                }
                // Temp Array to replace into original array
                int[] output = new int[k];
                for(i = k - 1; i >=0; i--) {
                    int lsdBucket = (a[i]/digit) % 10;
                    output[count[lsdBucket] - 1] = a[i];
                    count[lsdBucket] --;
                }
                for(i = 0; i < k; i += 1) {
                    a[i] = output[i];
                }
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
