import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Trang Van
 */
public class ECHashStringSetTest  {
    private ECHashStringSet table = new ECHashStringSet();
    private ECHashStringSet empty = new ECHashStringSet();
    private String[] dataset = new String[] {"A", "B","C","D","E","F"};


    @Test
    public void testContains () {
        for(String s: dataset) {
            table.put(s);
        }
        assertEquals(dataset.length, table.size());

        empty.put(null);
        assertEquals(0, empty.size());

        assertTrue(table.contains("A"));
        assertTrue(table.contains("B"));
        assertTrue(table.contains("C"));
        assertTrue(table.contains("D"));
        assertTrue(table.contains("E"));
        assertTrue(table.contains("F"));

        assertFalse(table.contains("G"));
        assertFalse(table.contains(""));
        assertFalse(empty.contains("A"));
    }

    @Test
    public void testAsList() {
        for(String s: dataset) {
            table.put(s);
        }
        assertTrue(table.asList() instanceof List);
        try{
            empty.asList();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }
}

