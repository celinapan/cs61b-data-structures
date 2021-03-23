/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        // FIXME: Add your code here.
        _indexCol1 = input.colNameToIndex(colName);
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        Table.TableRow currRow = _next;
        if(currRow.getValue(_indexCol1).contains(_subStr)) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    int _indexCol1;
    String _subStr;
}
