import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Trang Van
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testContains() {
        BSTStringSet alpha = new BSTStringSet();
        alpha.put("E");
        alpha.put("B");
        alpha.put("F");
        alpha.put("A");
        alpha.put("C");

        assertTrue(alpha.contains("B"));
        assertFalse(alpha.contains("D"));
    }

    @Test
    public void testAsList() {
        BSTStringSet alpha = new BSTStringSet();
        alpha.put("E");
        alpha.put("B");
        alpha.put("F");
        alpha.put("A");
        alpha.put("C");

        List<String> lst = alpha.asList();
        String[] expected = new String[] {"A", "B", "C", "E", "F"};
        assertArrayEquals(lst.toArray(), expected);
    }

}
