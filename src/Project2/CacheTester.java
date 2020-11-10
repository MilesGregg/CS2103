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
		public String get(Integer key) {
			return String.valueOf(key);
		}
	}

	/**
	 * A database that maps doubles to int arrays
	 */
	private static class Database2 implements DataProvider<Double, int[]> {
		@Override
		public int[] get(Double key) {
			return new int[] {(int) (double) key, (int) (double) key + 1};
		}
	}

	/**
	 * Tests that values retrieved from the dataprovider are actually stored in the cache
	 */
	@Test
	public void testStorage(){
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<>(provider, 3);
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
		Database provider = new Database();
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
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
	public void leastRecentlyUsedIsCorrect () {
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<Double, int[]>(provider, 2);
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

	@Test
	public void leastRecentlyUsedIsCorrect2 () {
		DataProvider<Integer, String> provider = new Database();
		Cache<Integer, String> cache = new LRUCache<>(provider, 2);
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
	 * Tests that the get() operations on the cache run in O(1) time complexity
	 */
	@Test(timeout=90000)
	public void testTimeComplexity () {
		int TOTAL_TRIALS = 10;
		int CAPACITY_MULTIPLIER = 1000;
		int NUM_GET_OPERATIONS = 100000;
		int NUM_K_VALUES = 100;
		double LOWER_BOUND = 0.1;
		double UPPER_BOUND = 0.9;
		Random rand = new Random();
		double sum = 0;
		// average the percentages over 10 trials
		for (int counter = 0; counter < TOTAL_TRIALS; counter++) {
			Database provider = new Database();
			// for 100 values of k, determine how long it takes the get() operation to run on a
			// cache of size 1000*k
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

	@Test
	public void testLRUWithLargeNumbers () {
		Database provider = new Database();
		// Need to instantiate an actual DataProvider
		Cache<Integer,String> cache = new LRUCache<>(provider, 500);
		// Every get() operation here is a miss, so the number of misses should increase by 1 each time
		// after this, the cache should store values from 250 to 749.
		for(int i = 0; i < 750; i++){
			cache.get(i);
			assertEquals(cache.getNumMisses(), i+1);
		}
		// every get() operation here is a hit, so the number of misses should not change
		for(int j = 749; j >= 250; j--){
			cache.get(j);
			assertEquals(cache.getNumMisses(), 750);
		}
		// every get() operation here is a miss again because all of these values have been evicted
		// so the number of misses should increase by 1 each time
		for(int k = 249; k >= 0; k--){
			cache.get(k);
			assertEquals(cache.getNumMisses(), 750 + 250 - k);
		}
	}

	@Test
	public void testLRUWithLargeNumbers2 () {
		Database2 provider = new Database2();
		// Need to instantiate an actual DataProvider
		Cache<Double, int[]> cache = new LRUCache<>(provider, 500);
		// Every get() operation here is a miss, so the number of misses should increase by 1 each time
		for(double i = 0; i < 750; i++) {
			cache.get(i);
			assertEquals(cache.getNumMisses(), (int) i+1);
		}
		// every get() operation here is a hit, so the number of misses should not change
		for(double j = 499; j >= 250; j--){
			cache.get(j);
			assertEquals(cache.getNumMisses(), 750);
		}
		// every get() operation here is a miss again, so the number of misses should increase by 1 each time
		for(double k = 249; k >= 0; k--){
			cache.get(k);
			assertEquals(cache.getNumMisses(), 750 + 250 - (int) k);
		}
	}

	@Test
	public void testEviction () {
		DataProvider<Integer, String> provider = new Database();
		Cache<Integer, String> cache = new LRUCache<>(provider, 4);
		// every get() operation here is a miss and should increase the number of misses by 1
		for(int i = 0; i < 4; i++)
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

	@Test
	public void testGetLRU () {
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<>(provider, 3);
		// these are all misses
		for(double i = 3.5; i < 6.5; i++)
			cache.get(i);
		assertEquals(cache.getNumMisses(), 3);
		// this is a hit on the least recently used item
		cache.get(3.5);
		assertEquals(cache.getNumMisses(), 3);
	}

	@Test
	public void testGetMRU () {
		DataProvider<Double, int[]> provider = new Database2();
		Cache<Double, int[]> cache = new LRUCache<>(provider, 3);
		// these are all misses
		for(double i = 3.5; i < 6.5; i++)
			cache.get(i);
		assertEquals(cache.getNumMisses(), 3);
		// this is a hit on the most recently used item
		cache.get(5.5);
		assertEquals(cache.getNumMisses(), 3);
	}

	@Test
	public void testProvider () {
		Database provider = new Database();
		final Cache<Integer,String> cache = new LRUCache<>(provider, 25);
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


	
}
