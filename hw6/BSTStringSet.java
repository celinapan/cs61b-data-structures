import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Trang Van
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet{
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    // START OF BSTStringSet METHOD IMPLEMENTATION
    @Override
    public void put(String s) {
        Node node = getLastNode(s);
        if (node == null) {
            _root = new Node(s);
        } else {
            if (s.compareTo(node.s) < 0) {
                node.left = new Node(s);
            } else if (s.compareTo(node.s) > 0) {
                node.right = new Node(s);
            }
        }
    }

    @Override
    public boolean contains(String s) {
        Node node = getLastNode(s);
        if (node != null && node.s.equals(s)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> l = new ArrayList<>();
        for (String s: this) {
            l.add(s);
        }
        return l;
    }

    /** Traverses down the tree to get to last available node.
     * Adapted from skeleton code I found from Fall 2016 after
     * my attempts weren't efficient enough.
     * @param s
     * @return Node
     */
    private Node getLastNode(String s) {
        Node _curr;
        _curr = _root;
        if (_root == null) {
            return null;
        }
        while (true) {
            Node next = null;
            if (s.compareTo(_curr.s) < 0) {
                next = _curr.left;
            } else if (s.compareTo(_curr.s) > 0) {
                next = _curr.right;
            }
            if (next == null || s.compareTo(_curr.s) == 0) {
                return _curr;
            } else {
                _curr = next;
            }
        }
    }
    // END OF BSTStringSet METHOD IMPLEMENTATION

    // START OF NODE CLASS
    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }
    // END OF NODE CLASS

    // START OF BOUND ITER CLASS
    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }
    // END OF REGULAR ITER CLASS


    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTBoundedIterator(_root, low, high);  // FIXME: PART B
    }

    // START BOUNDED ITER CLASS
    /** An BOUNDED iterator over BSTs. */
    private static class BSTBoundedIterator implements Iterator<String> {
        private Stack<Node> _toDo = new Stack<>();
        private String _low;
        private String _high;
        /** A new iterator over the labels in NODE. */
        BSTBoundedIterator(Node curr, String low, String high) {
            _low = low;
            _high = high;
            addTree(curr);
        }

        @Override
        public boolean hasNext() {
            String nextNode = _toDo.peek().s;
            return !_toDo.empty() && nextNode.compareTo(_high) <= 0;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null && node.s.compareTo(_low) >= 0) {
                _toDo.push(node);
                node = node.left;
            }

            if(node != null) {
                addTree(node.right);
            }
        }
    }

    //END OF BOUNDED ITER CLASS

    /** Root node of the tree. */
    private Node _root;
}
