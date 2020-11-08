package Project2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class BadCache <T, U> implements Cache <T, U> {

    private final ArrayList<Node> keyList = new ArrayList<>();
    private final ArrayList<U> valueList = new ArrayList<>();
    private final DataProvider <T, U> baseProvider;

    private int capacity = 0;
    private int numberMisses = 0;

    private Node mostRecent;
    private Node leastRecent;

    /**
     * @param baseProvider the data provider to consult for a cache miss
     */
    public BadCache(DataProvider<T, U> baseProvider, int capacity) {
        this.baseProvider = baseProvider;
        this.capacity = capacity;
    }

    /**
     * Returns the value associated with the specified key.
     * @param key the key
     * @return the value associated with the key
     */
    @Override
    public U get (T key) {
        int index = -1;
        for(Node node : keyList){
            if(node.key == key){
                index = node.value;
            }
        }
        if(index == -1) {
            U value = baseProvider.get(key);
            int newInd = valueList.size();
            keyList.add(new Node(key, newInd));
            valueList.add(value);
            if(valueList.size() > capacity) valueList.remove(0);
            return value;

        }
        return valueList.get(index);
    }

    /**
     * Returns the number of cache misses since the object's instantiation.
     * @return the number of cache misses since the object's instantiation.
     */
    @Override
    public int getNumMisses () {
        return numberMisses;
    }




    private class Node {
        T key;
        int value;


        private Node(T key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
