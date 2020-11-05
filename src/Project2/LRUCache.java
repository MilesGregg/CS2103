package Project2;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {

	private final Map<T, Node> map = new HashMap<>();
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

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		Node node;
		U value;
		System.out.println(map);

		if (this.map.containsKey(key)) {
			node = map.get(key);
			System.out.println(node.value);
			// make it move to front
			moveToFront(node, doubleLinkedList);
			value = node.value;
			System.out.println(node.value);

		}
		else {
			value = baseProvider.get(key);
			Node newNode = new Node(value, null, null);
			map.put(key, newNode);
			insert(newNode, doubleLinkedList);
			numberMisses++;
			if (map.size() > capacity) {
				doubleLinkedList.removeLast();
				map.remove(key);
			}

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

	private void insert (Node node, DoubleLinkedList doubleLinkedList) {
		doubleLinkedList.addToFront(node);
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
		if(node.prev != null)
			node.prev.next = node.next;
		node.next.prev = node.prev;
		node.prev = null;
		node.next = doubleLinkedList.head;
		doubleLinkedList.head = node;
	}

	private class Node{
		private U value;
		private Node next;
		private Node prev;

		private Node (U value, Node next, Node prev){
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

		public Node removeLast(){
			Node out = tail;
			tail.prev.next = null;
			tail = tail.prev;
			return out;
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

		public void addToFront(Node node){

			if (head == null) {
				head = node;
				tail = node;
			}
			else{
				head.prev = node;
				node.next = head;
				head = node;
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
