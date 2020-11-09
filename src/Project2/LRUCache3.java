package Project2;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 * @author Ravi Kirschner
 * @author Noah Darveau
 */
public class LRUCache3<T, U> implements Cache<T, U> {
    final private Map<T, Node> cache;
    final private DataProvider<T,U> provider;
    private final int capacity;
    private int timesMissed;
    private Node front;
    private Node back;
    /**
     * @param provider the data provider to consult for a cache miss
     * @param capacity the exact number of (key,value) pairs to store in the cache
     */
    public LRUCache3 (DataProvider<T, U> provider, int capacity) {
        cache = new HashMap<>(capacity);
        this.provider = provider;
        this.capacity = capacity;
        timesMissed = 0;
        front = null;
        back = null;
    }

    /**
     * Returns the value associated with the specified key.
     * @param key the key
     * @return the value associated with the key
     */
    public U get (T key) {
        if(cache.containsKey(key)) {
            final Node ret = cache.get(key);
            addToFront(evict(ret));
            return ret.obj;
            //if cache already contains the key, evict the value and move it to the front and return
        }
        else if(full()) {
            final Node bck = back;
            cache.remove(bck.key);
            evict(bck);
            //if we need to throw away something, then remove the back entry from the cache
            //and evict it from the LinkedList chain.
        }
        final Node ret = new Node(key, provider.get(key));
        addToFront(ret); //create a new Node with key and the provider's return
        //then add it to the front of the LinkedList
        cache.put(key, ret); //add this new entry to the cache
        timesMissed++; //increase timeMissed counter
        return ret.obj; //return
    }

    /**
     * Adds the specified object to the front of the LinkedList.
     * @param x The Node to add to the front of the list
     */
    private void addToFront(Node x) {
        x.after = front; //set x's "after" pointer to the current front
        x.before = null; //set x's "before" pointer to null, at it doesn't have anything before itself
        if(front != null) front.before = x; //if front isn't null, set fronts before pointer to x
        front = x; //set front to x
        if(back == null) back = front; //if there was no back previously, then x is both front and back
    }

    /**
     * Removes the specified object from the LinkedList
     * @param x The object to remove from the list
     * @return The object that was removed
     */
    private Node evict(Node x) {
        final Node newBefore = x.before; //the node before the one we're removing
        final Node newAfter = x.after; //the node after the one we're removing
        if(newAfter != null) {
            newAfter.before = newBefore;
            //if newAfter isn't null, change its "behind" pointer to the Node behind x
        }
        if(newBefore != null) {
            newBefore.after = newAfter;
            //if newBefore isn't null, change its "after" pointer to the Node after x
        }
        if(newBefore == null) {
            front = newAfter;
            //if newBefore is null (we are evicting the first element)
            //then set the new front to the node after x
        }
        if(newAfter == null) {
            back = newBefore;
            //if newAfter is null (we are evicting the last element)
            //then set the new back to the node before x
        }
        return x; //return object we evicted
    }
    /**
     * Returns the number of cache misses since the object's instantiation.
     * @return the number of cache misses since the object's instantiation.
     */
    public int getNumMisses () {
        return timesMissed;
    }

    /**
     * Inner class to hold the Node.
     * It is comparable to the Node class from class on 11/2/18
     */
    private class Node {
        final public U obj;
        final public T key;
        public Node before;
        public Node after;
        public Node(T key, U object) {
            obj = object;
            before = null;
            after = null;
            this.key = key;
        }
    }

    /**
     * Checks to see if the cache is going to overflow and if it will need to evict an element.
     * @return true if the cache will overflow, false otherwise.
     */
    private boolean full() {
        return cache.size() >= capacity;
    }
}
