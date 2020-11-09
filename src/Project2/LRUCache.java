package Project2;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache <T, U> implements Cache <T, U> {
    private final Map <T, Node> map = new HashMap <>();
    private final DataProvider <T, U> baseProvider;

    // tracks the node that is most recently used and the one that is least recently used
    private Node head;
    private Node tail;

    // max capacity of the cache
    private int capacity = 0;
    // tracks how many misses the cache finds and the max capacity of the cache
    private int numberMisses = 0;

    /**
     * @param provider the data provider to consult for a cache miss
     * @param capacity the exact number of (key,value) pairs to store in the cache
     */
    public LRUCache (DataProvider <T, U> provider, int capacity) {
        this.baseProvider = provider;
        this.capacity = capacity;
    }

    /**
     * Returns the value associated with the specified key, and updates the corresponding node's position in the linked list
     * @param key the key
     * @return the value associated with the key
     */
    public U get (T key) {
        Node node;

        // return node right away if it is already in the hashmap
        if (map.containsKey(key)) {
            node = map.get(key);
            moveToFront(node);
        } else {
            // increment the number of misses the cache has
            numberMisses++;

            // make new node to add to the hashmap
            node = new Node(key, baseProvider.get(key));

            // add the node to the linkedlist
            insert(node);

            // if the map is bigger than the capacity then remove a node from the hashmap and linked list
            if (map.size() > capacity) {
                // remove node from hashmap
                map.remove(tail.key);
                // make the next least recent node the least recent
                if (tail.next != null)
                    tail.next.previous = null;
                tail = tail.next;
            }
        }

        return node.value;
    }

    /**
     * Returns the number of cache misses since the object's instantiation.
     * @return the number of cache misses since the object's instantiation.
     */
    public int getNumMisses () {
        return numberMisses;
    }

    /**
     * move the current node to the mostRecent node
     * @param node - node object we are moving
     */
    private void moveToFront (Node node) {
        if(node.next != null)
            // creates a link from the next node to the previous node
            node.next.previous = node.previous;

        if (node.previous != null)
            // creates a link from the previous node to the next node
            node.previous.next = node.next;
        else
            // sets the next node as the least recently used node if it is the first node in the list
            tail = node.next;

        // Move the current node to the mostRecent
        node.previous = head;
        head.next = node;
        head = node;
        head.next = null;
    }

    /**
     * insert node into hashmap
     * @param node - node to be inserted into the hashmap
     */
    private void insert (Node node) {
        // insert into hashmap
        map.put(node.key, node);

        // update the leastRecent node with the node we are inserting
        if (tail == null)
            tail = node;
        // make the mostRecent the previous and the mostRecent next node the current node
        if (head != null) {
            node.previous = head;
            head.next = node;
        }

        // save the mostRecent node with the node we are inserting
        head = node;
    }

    /**
     * The Node class contains the contents for the doubly linked list for connecting the nodes to each other.
     * Each node has two pointers for the next and previous nodes in the linked list.
     */
    private class Node {
        // key and value values for the current node
        private final T key;
        private final U value;

        // next and previous nodes in the linked list
        Node next;
        Node previous;

        /**
         * setting up the node with their specified key and value values
         * @param key - the current node key
         * @param value - the current node value
         */
        private Node (T key, U value) {
            this.key = key;
            this.value = value;
        }
    }
}
