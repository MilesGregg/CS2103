package Project2;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache2 <T, U> implements Cache <T, U> {

    private final Map <T, Node> map = new HashMap <>();
    private final DataProvider <T, U> baseProvider;

    private int capacity = 0;
    private int numberMisses = 0;

    private Node mostRecent;
    private Node leastRecent;

    /**
     * @param provider the data provider to consult for a cache miss
     * @param capacity the exact number of (key,value) pairs to store in the cache
     */
    public LRUCache2 (DataProvider <T, U> provider, int capacity) {
        this.baseProvider = provider;
        this.capacity = capacity;
    }

    /**
     * Returns the value associated with the specified key.
     * @param key the key
     * @return the value associated with the key
     */
    @Override
    public U get (T key) {
        Node node;

        if (map.containsKey(key)) {
            node = map.get(key);
            moveToFront(node);
            // make it move to front
        } else {
            numberMisses++;

            node = new Node(key, baseProvider.get(key));
            insert(node);

            if (map.size() > capacity) {
                // remove
                this.map.remove(leastRecent.key);
                // make the next least recent node the least recent
                if (leastRecent.next != null)
                    leastRecent.next.previous = null;
                leastRecent = leastRecent.next;
            }
        }

        return node.value;
    }

    /**
     * Returns the number of cache misses since the object's instantiation.
     * @return the number of cache misses since the object's instantiation.
     */
    @Override
    public int getNumMisses () {
        return numberMisses;
    }

    private void moveToFront (Node node) {
//
//        if(node.next != null)
//            node.next.previous = node.previous;

        if (node.previous != null) {
            node.previous.next = node.next;
        }
        else {
            leastRecent = node.next;
        }

        node.next = null;
        node.previous = mostRecent;
        mostRecent.next = node;
        mostRecent = node;
    }

    private void insert (Node node) {
        this.map.put(node.key, node);

        if (leastRecent == null) {
            leastRecent = node;
        }

        if (mostRecent != null) {
            node.previous = mostRecent;
            mostRecent.next = node;
        }

        mostRecent = node;
    }

    private class Node {
        T key;
        U value;

        Node next;
        Node previous;

        private Node(T key, U value) {
            this.key = key;
            this.value = value;
        }
    }
}
