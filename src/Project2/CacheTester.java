import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTester {
	/**
	 * A database that maps Integers to Strings
	 */
	private static class Database implements DataProvider<Integer, String> {
		@Override
		public String get (Integer key) {
			return String.valueOf(key);
		}
	}

	/**
	 * A database that maps doubles to int arrays
	 */
	private static class Database2 implements DataProvider<Double, int[]> {
		@Override
		public int[] get (Double key) {
			return new int[] {(int) (double) key, (int) (double) key + 1};
		}
	}

	/**
	 * Tests that values retrieved from the dataprovider are actually stored in the cache
	 */
	@Test
	public void testStorage () {
		final int CAPACITY = 3;
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<>(provider, CAPACITY);
		cache.get(3.4);
		assertEquals(cache.getNumMisses(), 1);
		// these are both hits, so the number of misses should not change
		cache.get(3.4);
		cache.get(3.4);
		assertEquals(cache.getNumMisses(), 1);
	}

	/**
	 * Tests that the get() function works properly and that LRU eviction is implemented correctly
	 */
	@Test
	public void testGetAndEviction () {
		final int CAPACITY = 5;
		DataProvider<Integer, String> provider = new Database();
		Cache<Integer,String> cache = new LRUCache<>(provider, CAPACITY);
		// miss
		String x = cache.get(4);
		assertEquals(x, "4");
		// miss
		String y = cache.get(15);
		assertEquals(y, "15");
		String z = cache.get(-4);
		// miss
		assertEquals(z, "-4");
		assertEquals(cache.getNumMisses(), 3);
		// hit, getNumMisses() should still be 3
		cache.get(-4);
		assertEquals(cache.getNumMisses(), 3);
		// miss
		String b = cache.get(23);
		assertEquals(b, "23");
		// miss
		String c = cache.get(1);
		assertEquals(c, "1");
		assertEquals(cache.getNumMisses(), 5);
		// hit, getNumMisses() should still be 5
		cache.get(15);
		assertEquals(cache.getNumMisses(), 5);
		// miss
		cache.get(7);
		assertEquals(cache.getNumMisses(), 6);
		// miss because of eviction
		cache.get(4);
		assertEquals(cache.getNumMisses(), 7);
	}

	@Test
	public void testEviction2() {
		final int CAPACITY = 3;
		DataProvider<Integer,String> provider = new Database();
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, CAPACITY);
		// miss
		cache.get(4);
		// hit
		String x = cache.get(4);
		assertEquals(x, "4"); //checks that cache stores correct value
		assertEquals(cache.getNumMisses(), 1);
		// 3 misses
		cache.get(8);
		cache.get(10);
		cache.get(11);
		assertEquals(cache.getNumMisses(), 4);
		// miss because 4 was evicted
		cache.get(4);
		assertEquals(cache.getNumMisses(), 5);
	}

	/**
	 * Tests that the cache properly accesses and evicts items
	 */
	@Test
	public void leastRecentlyUsedIsCorrect () {
		DataProvider<Double, int[]> provider = new Database2();
		final int CAPACITY = 2;
		Cache<Double, int[]> cache = new LRUCache<Double, int[]>(provider, CAPACITY);
		cache.get(1.2);
		cache.get(4.5);
		// cache is now at capacity (storing 2 items)
		assertEquals(cache.getNumMisses(), 2);
		cache.get(9.8);
		assertEquals(cache.getNumMisses(), 3);
		// cache should have evicted 1.2, as it was the least recently used
		cache.get(1.2);
		// this should be a miss, so the number of misses should now be 4.
		assertEquals(cache.getNumMisses(), 4);
	}

	/**
	 * Tests that the cache properly accesses and evicts items
	 */
	@Test
	public void leastRecentlyUsedIsCorrect2 () {
		DataProvider<Integer, String> provider = new Database();
		final int CAPACITY = 2;
		Cache<Integer, String> cache = new LRUCache<>(provider, CAPACITY);
		cache.get(1);
		cache.get(4);
		// cache is now at capacity (storing 2 items)
		assertEquals(cache.getNumMisses(), 2);
		// hit, number of misses should not change
		cache.get(1);
		assertEquals(cache.getNumMisses(), 2);
		// miss
		cache.get(5);
		assertEquals(cache.getNumMisses(), 3);
		// cache should have evicted 4, as it was the least recently used
		cache.get(4);
		// this should be a miss because 4 was evicted, so the number of misses should now be 4.
		assertEquals(cache.getNumMisses(), 4);
	}

	/**
	 * for 100 values of k, determine how long it takes the get() operation to run on a
	 * cache of size 1000*k
	 * @return an array of the times
	 */
	private long[] getTimes() {
		final Random rand = new Random();
		final int CAPACITY_MULTIPLIER = 1000;
		final int NUM_GET_OPERATIONS = 100000;
		final int NUM_K_VALUES = 100;
		DataProvider<Integer, String> provider = new Database();
		long[] times = new long[NUM_K_VALUES];
		for (int k = 1; k <= NUM_K_VALUES; k++) {
			int capacity = CAPACITY_MULTIPLIER * k;
			Cache<Integer, String> cache1 = new LRUCache<>(provider, capacity);
			for (int q = 0; q < capacity; q++)
				cache1.get(q);
			// stores 100,000 random integers between 0 and capacity - 1
			int[] rands = new int[NUM_GET_OPERATIONS];
			for (int j = 0; j < rands.length; j++)
				rands[j] = rand.nextInt(capacity);
			// runs 100,000 get() operations and times it
			final long start1 = System.currentTimeMillis();
			for (int j = 0; j < NUM_GET_OPERATIONS; j++)
				cache1.get(rands[j]);
			final long end1 = System.currentTimeMillis();
			final long timeDiff1 = end1 - start1;
			// stores the time in an array
			times[k - 1] = timeDiff1;
		}
		return times;
	}

	/**
	 * Tests that the get() operations on the cache run in O(1) time complexity
	 */
	@Test(timeout=90000)
	public void testTimeComplexity () {
		final int TOTAL_TRIALS = 10;
		final double LOWER_BOUND = 0.1;
		final double UPPER_BOUND = 0.9;
		double sum = 0;
		// average the percentages over 10 trials
		for (int counter = 0; counter < TOTAL_TRIALS; counter++) {
			long[] times = getTimes();
			// find what percentage of the time times[j] > times[i] for j > i and what percentage
			// of the time times[j] = times[i] for j > i
			int greater = 0;
			int equal = 0;
			int trials = 0;
			for (int i = 0; i < times.length; i++) {
				for (int j = i + 1; j < times.length; j++) {
					trials++;
					if (times[j] > times[i])
						greater++;
					else if (times[j] == times[i])
						equal++;
				}
			}
			// records the percentage of the time when times[j] >= times[i] but only counts
			// the equal cases half as much in order to create balance.
			double greaterFraction = (double) greater / trials;
			double equalFraction = (double) equal / trials;
			sum += greaterFraction + equalFraction / 2;
		}
		double average = sum / TOTAL_TRIALS;
		// checks that the average percentage is between 0.1 and 0.9
		assertTrue(average <= UPPER_BOUND && average >= LOWER_BOUND);
	}

	/**
	 * Tests that the cache properly accesses and evicts the correct items for
	 * large numbers of elements
	 */
	@Test
	public void testLRUWithLargeNumbers () {
		final int TOTAL = 750;
		final int LAST_HIT = 250;
		final int CAPACITY = 500;
		DataProvider<Integer, String> provider = new Database();
		// Need to instantiate an actual DataProvider
		Cache<Integer,String> cache = new LRUCache<>(provider, CAPACITY);
		// Every get() operation here is a miss, so the number of misses should increase by 1 each time
		// after this, the cache should store values from 250 to 749.
		for (int i = 0; i < TOTAL; i++) {
			cache.get(i);
			assertEquals(cache.getNumMisses(), i+1);
		}
		// every get() operation here is a hit, so the number of misses should not change
		for (int j = TOTAL - 1; j >= LAST_HIT; j--) {
			cache.get(j);
			assertEquals(cache.getNumMisses(), 750);
		}
		// every get() operation here is a miss again because all of these values have been evicted
		// so the number of misses should increase by 1 each time
		for (int k = LAST_HIT - 1; k >= 0; k--) {
			cache.get(k);
			assertEquals(cache.getNumMisses(), 750 + 250 - k);
		}
	}

	/**
	 * Tests that the cache properly accesses and evicts the correct items for
	 * large numbers of elements
	 */
	@Test
	public void testLRUWithLargeNumbers2 () {
		final int TOTAL = 750;
		final int LAST_HIT = 250;
		final int CAPACITY = 500;
		DataProvider<Double, int[]> provider = new Database2();
		// Need to instantiate an actual DataProvider
		Cache<Double, int[]> cache = new LRUCache<>(provider, CAPACITY);
		// Every get() operation here is a miss, so the number of misses should increase by 1 each time
		for (double i = 0; i < TOTAL; i++) {
			cache.get(i);
			assertEquals(cache.getNumMisses(), (int) i+1);
		}
		// every get() operation here is a hit, so the number of misses should not change
		for (double j = TOTAL - 1; j >= LAST_HIT; j--) {
			cache.get(j);
			assertEquals(cache.getNumMisses(), 750);
		}
		// every get() operation here is a miss again, so the number of misses should increase by 1 each time
		for (double k = LAST_HIT - 1; k >= 0; k--) {
			cache.get(k);
			assertEquals(cache.getNumMisses(), 750 + 250 - (int) k);
		}
	}

	/**
	 * Tests that the cache can properly evict the least recently used item
	 */
	@Test
	public void testEviction () {
		DataProvider<Integer, String> provider = new Database();
		final int CAPACITY = 4;
		Cache<Integer, String> cache = new LRUCache<>(provider, CAPACITY);
		// every get() operation here is a miss and should increase the number of misses by 1
		for (int i = 0; i < CAPACITY; i++)
			cache.get(i);
		assertEquals(cache.getNumMisses(), 4);
		cache.get(0);
		// this is a hit and should not change the number of misses
		assertEquals(cache.getNumMisses(), 4);
		// this is a miss
		cache.get(5);
		assertEquals(cache.getNumMisses(), 5);
		// this is a miss because "1" has been evicted
		cache.get(1);
		assertEquals(cache.getNumMisses(), 6);
		// this is a hit because "3" is still in the cache
		cache.get(3);
		assertEquals(cache.getNumMisses(), 6);
	}

	/**
	 * Tests that the cache can properly access the least recently used item
	 */
	@Test
	public void testGetLRU () {
		final int CAPACITY = 3;
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<>(provider, CAPACITY);
		// these are all misses
		for (double i = 3.5; i < 6.5; i++)
			cache.get(i);
		assertEquals(cache.getNumMisses(), 3);
		// this is a hit on the least recently used item
		cache.get(3.5);
		assertEquals(cache.getNumMisses(), 3);
	}

	/**
	 * Tests that the cache can properly access the most recently used item
	 */
	@Test
	public void testGetMRU () {
		final int CAPACITY = 3;
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<>(provider, CAPACITY);
		// these are all misses
		for (double i = 3.5; i < 6.5; i++)
			cache.get(i);
		assertEquals(cache.getNumMisses(), 3);
		// this is a hit on the most recently used item
		cache.get(5.5);
		assertEquals(cache.getNumMisses(), 3);
	}

	/**
	 * Checks that the cache properly stores data
	 */
	@Test
	public void testCacheMemory() {
		final int CAPACITY = 25;
		DataProvider<Integer, String> provider = new Database();
		final Cache<Integer,String> cache = new LRUCache<>(provider, CAPACITY);
		// miss
		assertEquals(cache.get(5), "5");
		assertEquals(cache.getNumMisses(), 1);
		// miss
		assertEquals(cache.get(7), "7");
		assertEquals(cache.getNumMisses(), 2);
		// miss
		assertEquals(cache.get(3), "3");
		assertEquals(cache.getNumMisses(), 3);
		// miss
		assertEquals(cache.get(10), "10");
		assertEquals(cache.getNumMisses(), 4);
		// hit
		assertEquals(cache.get(7), "7");
		assertEquals(cache.getNumMisses(), 4);
		// hit
		assertEquals(cache.get(5), "5");
		assertEquals(cache.getNumMisses(), 4);
		// miss
		assertEquals(cache.get(2), "2");
		assertEquals(cache.getNumMisses(), 5);
	}

	@Test
	public void testZeroCapacity() {
		int NUM_TESTS = 25;
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<>(provider, 0);
		// everything is a miss with capacity 0
		for (int i = 0; i < NUM_TESTS; i++) {
			cache.get((double) i);
			assertEquals(cache.getNumMisses(), i+1);
		}
		// even when they all get the same value, the cache has 0 memory so everything is a miss
		for (int j = 0; j < NUM_TESTS; j++) {
			cache.get(0.0);
			assertEquals(cache.getNumMisses(), 26+j);
		}
		assertEquals(cache.getNumMisses(), 50);
	}

}
