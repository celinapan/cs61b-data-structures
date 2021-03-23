package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BACD)             (XYZ)",
                new Alphabet("ABCDXYZ"));
        Permutation p3 = new Permutation("(1234)", new Alphabet("1234"));
        Permutation p4 = new Permutation("(#@$%)", new Alphabet("#@$%"));
        Permutation p5 = new Permutation("(asdfg)", new Alphabet("asdfg"));

        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('A', p.invert('C'));
        assertEquals('C', p.invert('D'));

        assertEquals('B', p2.invert('A'));
        assertEquals('D', p2.invert('B'));
        assertEquals('A', p2.invert('C'));
        assertEquals('C', p2.invert('D'));
        assertEquals('Z', p2.invert('X'));
        assertEquals('X', p2.invert('Y'));
        assertEquals('Y', p2.invert('Z'));

        assertEquals('4', p3.invert('1'));
        assertEquals('1', p3.invert('2'));
        assertEquals('2', p3.invert('3'));
        assertEquals('3', p3.invert('4'));

        assertEquals('%', p4.invert('#'));
        assertEquals('#', p4.invert('@'));
        assertEquals('@', p4.invert('$'));
        assertEquals('$', p4.invert('%'));

        assertEquals('g', p5.invert('a'));
        assertEquals('a', p5.invert('s'));
        assertEquals('s', p5.invert('d'));
        assertEquals('d', p5.invert('f'));
        assertEquals('f', p5.invert('g'));
    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BACD) (XYZ)",
                            new Alphabet("ABCDXYZ"));
        Permutation p3 = new Permutation("(1234)", new Alphabet("1234"));
        Permutation p4 = new Permutation("(#@$%)", new Alphabet("#@$%"));
        Permutation p5 = new Permutation("(asdfg)", new Alphabet("asdfg"));

        assertEquals('C', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));

        assertEquals('C', p2.permute('A'));
        assertEquals('A', p2.permute('B'));
        assertEquals('D', p2.permute('C'));
        assertEquals('B', p2.permute('D'));
        assertEquals('Y', p2.permute('X'));
        assertEquals('Z', p2.permute('Y'));
        assertEquals('X', p2.permute('Z'));

        assertEquals('2', p3.permute('1'));
        assertEquals('3', p3.permute('2'));
        assertEquals('4', p3.permute('3'));
        assertEquals('1', p3.permute('4'));

        assertEquals('@', p4.permute('#'));
        assertEquals('$', p4.permute('@'));
        assertEquals('%', p4.permute('$'));
        assertEquals('#', p4.permute('%'));

        assertEquals('s', p5.permute('a'));
        assertEquals('d', p5.permute('s'));
        assertEquals('f', p5.permute('d'));
        assertEquals('g', p5.permute('f'));
        assertEquals('a', p5.permute('g'));

    }

    @Test
    public void testInvertInt() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));

        assertEquals(1, p.invert(0));
        assertEquals(3, p.invert(1));
        assertEquals(0, p.invert(2));
        assertEquals(2, p.invert(3));

        assertEquals(2, p.invert(-1));
        assertEquals(3, p.invert(5));

        Permutation p2 = new Permutation("(BACD) (XYZ)",
                new Alphabet("ABCDXYZ"));

        assertEquals(1, p2.invert(0));
        assertEquals(3, p2.invert(1));
        assertEquals(0, p2.invert(2));
        assertEquals(2, p2.invert(3));
        assertEquals(6, p2.invert(4));
        assertEquals(4, p2.invert(5));
        assertEquals(5, p2.invert(6));

        assertEquals(6, p2.invert(-3));
        assertEquals(1, p2.invert(7));
    }

    @Test
    public void testPermuteInt() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BACD) (XYZ)",
                new Alphabet("ABCDXYZ"));

        assertEquals(2, p.permute(0));
        assertEquals(0, p.permute(1));
        assertEquals(3, p.permute(2));
        assertEquals(1, p.permute(3));

        assertEquals(2, p2.permute(0));
        assertEquals(0, p2.permute(1));
        assertEquals(3, p2.permute(2));
        assertEquals(1, p2.permute(3));
        assertEquals(5, p2.permute(-3));
        assertEquals(1, p2.permute(10));
    }

    @Test
    public void testSize() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BACD) (XYZ) (W)",
                new Alphabet("ABCDWXYZ"));
        assertEquals(4, p.alphabet().size());
        assertEquals(8, p2.alphabet().size());
    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(HIG)(NF) (L)",
                new Alphabet("HILFNGR"));
        Permutation p2 = new Permutation("(BACD) (XYZ)",
                new Alphabet("ABCDXYZ"));
        Permutation p3 = new Permutation("(A) (B) (C) (D)",
                new Alphabet("ABCD"));

        assertFalse(p.derangement());
        assertTrue(p2.derangement());
        assertFalse(p3.derangement());
    }

    @Test
    public void testAlphabet() {
        Alphabet a =  new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals(a, p.alphabet());
    }

}
