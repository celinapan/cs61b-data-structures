import static org.junit.Assert.*;

public class Utils {
    public static int max(int [] a) {
        assertTrue(a.length > 0);
        int maxVal = a[0];
        for (int i = 1; i < a.length; i += 1) {
            if (a[i] > maxVal) {
                maxVal = a[i];
            }
        }
        return maxVal;
    }

    public static boolean threeSum(int [] a) {
        for (int i = 0; i < a.length; i += 1) {
            for (int j = 0; j < a.length; j += 1) {
                for (int k = 0; k < a.length; k += 1) {
                    if (a[i] + a[j] + a[k] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean threeSumDistinct(int [] a) {
        for (int i = 0; i < a.length; i += 1) {
            for (int j = i + 1; j < a.length; j += 1) {
                for (int k = j + 1; k < a.length; k += 1) {
                    if (a[i] + a[j] + a[k] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
