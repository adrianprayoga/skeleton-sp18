package lab9;

import edu.princeton.cs.algs4.In;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        } else if (p.key.equals(key)) {
            return p.value;
        } else if (p.key.compareTo(key) > 0) {
            return getHelper(key, p.left);
        } else if (p.key.compareTo(key) < 0) {
            return getHelper(key, p.right);
        } else  {
            return null;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {

        if (p == null) {
            return new Node(key, value);
        } else if (p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        } else if (p.key.compareTo(key) < 0) {
            p.right = putHelper(key, value, p.right);
        } else if (p.key.equals(key)) {
            p.value = value;
        }

        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
        size += 1;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void helperKeySet(Set<K> keys, Node p) {
        if (p == null) {
            return;
        } else {
            keys.add(p.key);
            helperKeySet(keys, p.left);
            helperKeySet(keys, p.right);
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set keys = new HashSet<K>();
        helperKeySet(keys, root);
        return keys;
    }

    private boolean isLeafNode (Node p) {
        return p.left == null && p.right == null;
    }



    private Node helperRemove(K key, Node p) {
        if (p == null) {
            return null;
        } else if (p.key.compareTo(key) > 0) {
            p.left  = helperRemove(key, p.left);
        } else if (p.key.compareTo(key) < 0) {
            p.right  = helperRemove(key, p.right);
        } else {
            // this handles the case of leafNode / node having 1 child
            // there is no need to check whether it's a leaf node or not
            if (p.right == null) return p.left;
            if (p.left == null) return p.right;

            // this is to handle deletion for node with 2 children
            Node t = p;
            Node leftMost = getLeftMostNode(t.right);
            p.key = leftMost.key;
            p.value = leftMost.value;

            p.right = deleteLeftMostNode(t.right);
            p.left = t.left;

            return p;
        }

        return p;
    }

    private Node getLeftMostNode(Node p) {
        if (p.left == null) {
            return p;
        }
        return getLeftMostNode(p.left);
    }

    private Node deleteLeftMostNode(Node p) {
        if (p.left == null) {
            return p.right;
        }

        p.left = getLeftMostNode(p.left);
        return p;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        size -= 1;
        V tmp = get(key);
        if (tmp != null) {
            root = helperRemove(key, root);
        }
        return tmp;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private void getStringRep(Node p) {
        if (p == null) return;

        if (p.left != null) {
            System.out.println(p.key + " -(left)-> " + p.left.key);
        }
        if (p.right != null) {
            System.out.println(p.key + " -(right)-> " + p.right.key);
        }

        getStringRep(p.left);
        getStringRep(p.right);
    }

    @Override
    public String toString() {
        getStringRep(root);
        return "";
    }

    public static void main(String[] args) {
        BSTMap<Integer, Integer> bstmap = new BSTMap<>();
        bstmap.put(12, 5);
        bstmap.put(20, 10);
        bstmap.put(7, 22);
        bstmap.put(5, 90);
        bstmap.put(9, 90);
        bstmap.put(8, 90);
        bstmap.put(10, 90);
        bstmap.put(25, 90);
        bstmap.put(21, 90);


        System.out.println(bstmap.get(7));
        System.out.println(bstmap.keySet());
        System.out.println(bstmap);

        bstmap.remove(7);
        System.out.println(bstmap.keySet());
        System.out.println(bstmap);
    }
}
