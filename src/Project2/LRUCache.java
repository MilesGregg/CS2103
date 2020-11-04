package Project2;

import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {

	private final HashMap<T, DoubleLinkedList> map = new HashMap<T, DoubleLinkedList>();
	private final DataProvider<T, U> baseProvider;

	private int capacity = 0;
	private int numberMisses = 0;

	private DoubleLinkedList mostRecent;
	private DoubleLinkedList leastRecent;

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		this.baseProvider = provider;
		this.capacity = capacity;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		DoubleLinkedList doubleLinkedList;

		if (this.map.containsKey(key)) {
			doubleLinkedList = map.get(key);
			// make it move to front
		} else {
			if (map.size() > capacity) {
				// remove
			}

			doubleLinkedList = new DoubleLinkedList(key, baseProvider.get(key));
			insert(doubleLinkedList);

			numberMisses++;
		}

		return doubleLinkedList.value;  // TODO -- implement!
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return numberMisses;
	}

	private void insert (DoubleLinkedList doubleLinkedList) {
		this.map.put(doubleLinkedList.key, doubleLinkedList);

		if (leastRecent == null) {
			leastRecent = doubleLinkedList;
		}

		if (mostRecent != null) {
			doubleLinkedList.previous = mostRecent;
			mostRecent.next = doubleLinkedList;
		}

		mostRecent = doubleLinkedList;
	}

	private class DoubleLinkedList {
		T key;
		U value;

		DoubleLinkedList next;
		DoubleLinkedList previous;

		private DoubleLinkedList(T key, U value) {
			this.key = key;
			this.value = value;
		}
	}
}
