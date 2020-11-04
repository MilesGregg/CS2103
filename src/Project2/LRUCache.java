package Project2;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {

	private final Map<T, U> map = new HashMap<>();
	private final DataProvider<T, U> baseProvider;
	private final DoubleLinkedList doubleLinkedList = new DoubleLinkedList();

	private int capacity = 0;
	private int numberMisses = 0;

	private Node mostRecent;
	private Node leastRecent;

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		this.baseProvider = provider;
		this.capacity = capacity;
	}

	private Node search(DoubleLinkedList list, T key){
		Node current = list.head;
		while(current.value != key) current = current.next;
		return current;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		U value;

		if (this.map.containsKey(key)) {
			value = map.get(key);
			// make it move to front
			moveToFront(search(doubleLinkedList, key), doubleLinkedList);

		}
		else {
			if (map.size() > capacity) {
				// remove
			}
			value = baseProvider.get(key);
			map.put(key, value);

			insert(key, doubleLinkedList);
			numberMisses++;
		}

		return value;  // TODO -- implement!
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return numberMisses;
	}

	private void insert (T key, DoubleLinkedList doubleLinkedList) {
		doubleLinkedList.addToFront(key);
		if (leastRecent == null) {
			leastRecent = doubleLinkedList.head;
		}

		if (mostRecent != null) {
			doubleLinkedList.head = mostRecent;
			mostRecent.next = doubleLinkedList.head;
		}

		mostRecent = doubleLinkedList.head;
	}

	private void moveToFront(Node node, DoubleLinkedList doubleLinkedList){
		node.prev.next = node.next;
		node.prev = null;
		node.next = doubleLinkedList.head;
		doubleLinkedList.head = node;
	}

	private class Node{
		T value;
		Node next;
		Node prev;

		private Node (T value, Node next, Node prev){
			this.value = value;
			this.next = next;
			this.prev = prev;
		}
	}

	private class DoubleLinkedList {
		Node head;
		Node tail;

		private DoubleLinkedList() {
			head = null;
			tail = null;
		}
		public void add(Node value){
			if (head == null) {
				head = value;
				tail = value;
			}
			else {
				tail.next = value;
				value.prev = tail;
				tail = value;
			}
		}

		public void addToFront(T key){
			Node value = new Node(key, null, null);
			if (head == null) {
				head = value;
				tail = value;
			}
			else{
				head.prev = value;
				value.next = head;
				head = value;
			}
		}
		private void remove(Node value) {
			if(value.next == null)
				value.prev.next = null;
			value.prev.next = value.next;
			value.next.prev = value.prev;
		}
	}

}
