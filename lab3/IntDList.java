/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value
        int size = 0;
        DNode temp = _front;
        while (temp != null){
            size += 1;
            temp = temp._next;
        }
        return size;
    }


    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        // FIXME: Implement this method and return correct value
        int k = 0;
        DNode temp = _front;
        if (i < 0){
            i += size();
        }
        while (k != i){
            temp = temp._next;
            k += 1;
        }
        return temp._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        DNode newNode = new DNode(d);
        if (size() == 0){
            _front = _back = newNode;
        } else {
            newNode._next = _front;
            newNode._prev = null;
            _front._prev = newNode;
            _front = newNode;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this method
        DNode newNode = new DNode(d);
        if (size() == 0){
            _front = _back = newNode;
        } else {
            newNode._prev = _back;
            newNode._next = null;
            _back._next = newNode;
            _back = newNode;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        if (index == 0 || index <= -size()){
            insertFront(d);
        } else if (index == -1 || index >= size()){
            insertBack(d);
        } else {
            int k = 0;
            DNode newNode = new DNode(d);
            DNode temp = _front;
            if (index < 0) {
                for (int i = 0; i < index + size(); i += 1) {
                    temp = temp._next;
                }

                newNode._next = temp._next;
                newNode._prev = temp;
                temp._next._prev = newNode;
                temp._next  = newNode;
            } else {
                for (int i = 0; i < index; i += 1) {
                    temp = temp._next;
                }
                newNode._prev = temp._prev;
                newNode._next = temp;
                temp._prev._next = newNode;
                temp._prev = newNode;
            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct value
        DNode temp = _front;
        if (size() == 1){
            _front = _back = null;
            return temp._val;
        }
        _front = temp._next;
        return temp._val;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
        int targetVal;
        DNode temp = _back;
        targetVal = temp._val;
        if (size() == 1){
            _front = _back = null;
            return targetVal;
        }
        _back = temp._prev;
        _back._next = null;
        temp._prev = null;

        return targetVal;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        int targetVal = 0;
        if (index == 0 || index <= -size()){
            targetVal = deleteFront();
        } else if (index == -1 || index >= size() - 1){
            targetVal = deleteBack();
        } else{
            DNode temp = _front;

            if (index < 0) {
                for (int i = 0; i < index + size(); i += 1) {
                    temp = temp._next;
                }

                targetVal = temp._val;
                temp._prev._next = temp._next;
                temp._next._prev = temp._prev;
                temp._prev = temp._next = null;
            } else {
                for (int i = 0; i < index; i += 1) {
                    temp = temp._next;
                }
                targetVal = temp._val;
                temp._prev._next = temp._next;
                temp._next._prev = temp._prev;
                temp._prev = temp._next = null;
            }

        }

        return targetVal;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

    public static void main(String[] args){

        IntDList d = new IntDList(5, 10, 20);
        System.out.println("Size <3>:" + d.size());
        System.out.println("Get 2nd element:" + d.get(1));
        d.insertFront(0);
        System.out.println("Insert 0 to front: " + d.get(0));
        d.insertBack(25);
        System.out.println("Insert 25 to back: " + d.get(d.size()-1));
        d.insertAtIndex(15, 2);
        System.out.println("Insert 15 at index 2 <0, 5, 10, 15,20, 25>: " + d.get(0));
        System.out.println(d.get(1));
        System.out.println(d.get(2));
        System.out.println(d.get(3));
        System.out.println(d.get(4));
        System.out.println(d.get(5));
        System.out.println("Size <6>:" + d.size());
        System.out.println("Deleted: "+ d.deleteFront());
        System.out.println("Size <5>:" + d.size());
        System.out.println("Get 2nd element (10):" + d.get(1));
        System.out.println("Deleted: "+ d.deleteBack());
        System.out.println("Size <4>:" + d.size());
        System.out.println("Get 2nd element (20):" + d.get(d.size()-1));
        System.out.println("Insert 15 at index 2 <5, 10, 15,20>: " + d.get(0));
        System.out.println(d.get(1));
        System.out.println(d.get(2));
        System.out.println(d.get(3));

        System.out.println("Delete at index 1 <10>: "+ d.deleteAtIndex(1));
        System.out.println("Size <3>:" + d.size());
        System.out.println("Get 2nd element (15):" + d.get(1));




    }
}
