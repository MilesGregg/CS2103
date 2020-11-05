package Project2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTester {

	private class Database<T, U> implements DataProvider<T, U> {
		private Map<T, U> data;

		private Database(){
			data = new HashMap<>();
		}

		public void insert(T key, U value){
			data.put(key, value);
		}

		@Override
		public U get(T key) {
			return data.get(key);
		}

		public int size(){
			return data.size();
		}
	}
	@Test
	public void leastRecentlyUsedIsCorrect () {
		Database<Integer, String> provider = new Database<>();
		provider.insert(4, "Win");
		provider.insert(7, "Fan");
		provider.insert(14, "Australia");
		provider.insert(-4, "Watermelon");
		provider.insert(15, "Notebook");
		provider.insert(1, "DVD");
		provider.insert(23, "CD");

		// Need to instantiate an actual DataProvider
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
		String x = cache.get(4);
		assertEquals(x, "Win");
		String y = cache.get(15);
		assertEquals(y, "Notebook");
		String z = cache.get(-4);
		assertEquals(z, "Watermelon");
		assertEquals(cache.getNumMisses(), 3);
		String a = cache.get(-4);
		assertEquals(cache.getNumMisses(), 3);
		String b = cache.get(23);
		assertEquals(b, "CD");
		String c = cache.get(1);
		assertEquals(c, "DVD");
		assertEquals(cache.getNumMisses(), 5);
		String d = cache.get(15);
		assertEquals(cache.getNumMisses(), 5);
		String f = cache.get(7);
		assertEquals(cache.getNumMisses(), 5);
		String e = cache.get(4);
		assertEquals(cache.getNumMisses(), 6);
	}
}
