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
    private Node mostRecentNode;
    private Node leastRecentNode;
    // tracks how many misses the cache finds and the max capacity of the cache
    private final int capacity;
    private int numberMisses = 0;

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
    public U get (T key) {
        Node node;
        // return node right away if it is already in the hashmap(linked list)
        if (this.map.containsKey(key)) {
            node = this.map.get(key);
            moveNodeToFront(node);
        } else {
            // count the number of misses the cache does
            this.numberMisses++;

            // make new node to add to the hashmap(linked list)
            node = new Node(key, this.baseProvider.get(key));
            insertNode(node);

            // if the map is bigger than the capacity then remove a node from the hashmap(linked list)
            if (this.map.size() > this.capacity) {
                // remove node from hashmap
                this.map.remove(this.leastRecentNode.key);
                // make the next least recent node from least recent
                if (this.leastRecentNode.next != null)
                    this.leastRecentNode.next.previous = null;
                this.leastRecentNode = this.leastRecentNode.next;
            }
        }
        // return the node value that we found
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
    private void moveNodeToFront (Node node) {
        // if the mostRecent node equals the node we are moving than break out of method
        if (mostRecentNode.equals(node))
            return;

        if(node.next != null)
            node.next.previous = node.previous;

        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            leastRecentNode = node.next;
        }
        // Move the current node to the mostRecent
        node.next = null;
        node.previous = mostRecentNode;
        mostRecentNode.next = node;
        mostRecentNode = node;
    }

    /**
     * insert node into hashmap
     * @param node - node to be inserted into the hashmap
     */
    private void insertNode (Node node) {
        // insert into hashmap
        this.map.put(node.key, node);

        // make the mostRecent the
        if (mostRecentNode != null) {
            node.previous = mostRecentNode;
            mostRecentNode.next = node;
        }

        // update the leastRecent node with the node we are inserting
        if (leastRecentNode == null) {
            leastRecentNode = node;
        }
        // save the mostRecent node with the node we are inserting
        mostRecentNode = node;
    }

    /**
     * The Node class contains the contents for the doubly linked list for connecting the nodes to each other.
     * Each node has two pointers for the next and previous nodes in the linked list.
     */
    private class Node {
        // key and value values for the current node
        T key;
        U value;

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
