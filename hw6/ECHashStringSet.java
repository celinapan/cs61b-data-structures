import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Trang Van
 */
class ECHashStringSet implements StringSet {

    ECHashStringSet() {
        _numElements = 0;
        _table = new LinkedList[MAX_LF];
        for(int i = 0; i < countBuckets(); i += 1) {
            _table[i] = new LinkedList<>();
        }
    }

    @Override
    public void put(String s) {
        if(s != null) {
            int id = hash(s);
            if (loadFactor() >= MAX_LF) {
                resize();
            }
            _table[id].add(s);
            _numElements += 1;
        }
    }

    private void resize() {
        LinkedList<String>[] _curr = _table;
        _table = new LinkedList[2 * _curr.length];
        _numElements = 0;
        for(int i = 0; i < countBuckets(); i += 1) {
            _table[i] = new LinkedList<>();
        }
        for (LinkedList<String> b : _curr) {
            if (b != null) {
                for (String s : b) {
                    _table[hash(s)].add(s);
                }
            }
        }
    }

    @Override
    public boolean contains(String s) {
        if(s != null && _table[hash(s)] != null) {
            return _table[hash(s)].contains(s);
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> l = new ArrayList<>();
        for (LinkedList<String> bucket : _table) {
            if(bucket == null) {
                return l;
            } else {
                for(String s: bucket) {
                    l.add(s);
                }
            }

        }
        return l;
    }
    public int hash(String s) {
        return s.hashCode() & 0x7fffffff % countBuckets();
    }
    public int countBuckets() {
        return _table.length;
    }

    public double loadFactor() {
        return ((double) size())/((double)countBuckets());
    }

    public int size() {
        return _numElements;
    }

    private int _numElements;
    private LinkedList<String>[] _table;
    private static final double MIN_LF = 0.2;
    private static final int MAX_LF = 5;
}
