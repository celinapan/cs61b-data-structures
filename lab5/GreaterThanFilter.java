/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        // FIXME: Add your code here.
        _indexCol1 = input.colNameToIndex(colName);
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        Table.TableRow currRow = _next;
        String compVal = currRow.getValue(_indexCol1);
        if(compVal.compareTo(_ref) > 0) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    int _indexCol1;
    String _ref;
}
