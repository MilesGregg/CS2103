package Project2;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache3 <T, U> implements Cache <T, U> {

    private final Map <T, Node> map = new HashMap <>();
    private final DataProvider <T, U> baseProvider;

    private int capacity = 0;
    private int numberMisses = 0;

    private Node head;
    private Node tail;

    /**
     * @param provider the data provider to consult for a cache miss
     * @param capacity the exact number of (key,value) pairs to store in the cache
     */
    public LRUCache3 (DataProvider <T, U> provider, int capacity) {
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
            addFirst(node);

            if (map.size() > capacity) {
                map.remove(tail.key);
                removeNode(tail);
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

    private void moveToFront(Node node) {
        removeNode(node);
        addFirst(node);
    }

    private void addFirst(Node node) {
        node.next = head;
        node.previous = null;

        if (head != null) {
            head.previous = node;
        }
        head = node;

        if (tail == null) {
            tail = node;
        }
    }

    private void removeNode(Node node) {
        Node prevNode = node.previous;
        Node nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.previous = prevNode;
        } else {
            tail = prevNode;
        }
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
