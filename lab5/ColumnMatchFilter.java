/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        // FIXME: Add your code here.
        _indexCol1 = input.colNameToIndex(colName1);
        _indexCol2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        Table.TableRow currRow = _next;
        if (currRow.getValue(_indexCol1).equals(currRow.getValue(_indexCol2))) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    int _indexCol1, _indexCol2;
}
