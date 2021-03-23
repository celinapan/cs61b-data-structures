/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        // FIXME: Add your code here.
        _indexCol1 = input.colNameToIndex(colName);
        _match = match;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        Table.TableRow currRow = _next;
        if(currRow.getValue(_indexCol1).equals( _match)) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    int _indexCol1;
    String _match;
}
