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
		Cache<Integer,String> cache = new LRUCache2<>(provider, 5);
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
		assertEquals(cache.getNumMisses(), 6);
		String e = cache.get(4);
		assertEquals(cache.getNumMisses(), 7);
	}

	@Test
	public void testTimeStuff () {
		Database<Integer, String> provider = new Database<>();
		for(int i = 0; i < 100000; i++){
			provider.insert(i, String.valueOf((int) (Math.random()*100000)));
		}
		Cache<Integer,String> cache1 = new LRUCache2<>(provider, 10);
		Cache<Integer,String> cache2 = new LRUCache2<>(provider, 1000);
		double cache1Longer = 0;
		for(int j = 0; j < 100; j++) {
			final long start1 = System.currentTimeMillis();
			for(int k = 0; k < 100000; k++)
				cache1.get((int) (Math.random()*100000));
			final long end1 = System.currentTimeMillis();
			final long timeDiff1 = end1 - start1;

			final long start2 = System.currentTimeMillis();
			for(int k = 0; k < 100000; k++)
				cache2.get((int) (Math.random()*100000));
			final long end2 = System.currentTimeMillis();
			final long timeDiff2 = end2 - start2;

			if(timeDiff1 < timeDiff2)
				cache1Longer += 1;
		}
		cache1Longer /= 100;
		System.out.println(cache1Longer);
		assertTrue(cache1Longer <= 0.6 && cache1Longer >= 0.4);

	}
}
