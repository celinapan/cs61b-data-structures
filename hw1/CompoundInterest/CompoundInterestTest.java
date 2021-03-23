import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        assertEquals(0, 0); */
        assertEquals(23, CompoundInterest.numYears(2043));
        assertEquals(5, CompoundInterest.numYears(2025));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2022), tolerance);
        assertEquals(4466.99, CompoundInterest.futureValue(3500, 5, 2025), tolerance);

    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10, 12, 2022, 3), tolerance);
        assertEquals(3456.471710, CompoundInterest.futureValueReal(3500, 5, 2025, 5), tolerance);

    }


    @Test
    public void testTotalSavings() {

        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2022, 10), tolerance);

    }

    @Test
    public void testTotalSavingsReal() {

        double tolerance = 0.01;
        assertEquals(15571.90, CompoundInterest.totalSavingsReal(5000, 2022, 10, 3), tolerance);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
