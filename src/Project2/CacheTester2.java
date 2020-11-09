package Project2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTester2 {
	final int ITERATIONS = 100;

	public class MyDataProvider implements DataProvider<Integer, Integer> {
		private int numFetches;
		public MyDataProvider(){
			numFetches = 0;
		}


		/**
		 * Returns the number of time the data provider is called to add data to the cache
		 * @return the number of time the data provider is called to add data to the cache
		 */
		public int getNumFetches(){
			return numFetches;
		}

		@Override
		public Integer get(Integer key) {
			numFetches++;
			return key;
		}
	}

	@Test
	/**
	 * Tests if the most recently used character is in the correct location
	 */
	public void leastRecentlyUsedIsCorrect () {
		MyDataProvider provider = new MyDataProvider(); // Need to instantiate an actual DataProvider
		Cache<Integer,Integer> cache = new LRUCache3<>(provider, 5);
		for (int i = 0; i < 5; i++){
			int t = cache.get(i); // throw the value away
		}

		assertEquals(cache.get(4), new Integer(4));
		assertEquals(provider.getNumFetches(), 5);
	}

	@Test
	/**
	 * Tests if the cache removes the least used values and tries to remove an element from the beginning of the cache
	 */
	public void removesUnused () {
		MyDataProvider provider = new MyDataProvider(); // Need to instantiate an actual DataProvider
		Cache<Integer,Integer> cache = new LRUCache3<>(provider, 5);

		for (int i = 0; i < 6; i++){
			int t = cache.get(i); // throw the value away
		}
		assertEquals(cache.get(1), new Integer(1));
		assertEquals(cache.get(2), new Integer(2));
		assertEquals(cache.get(3), new Integer(3));
		assertEquals(cache.get(4), new Integer(4));
		assertEquals(cache.get(5), new Integer(5));
		assertEquals(provider.getNumFetches(), 6);
	}

	@Test
	/**
	 * Tries to add an element to the cache
	 */
	public void testAddToCache(){
		MyDataProvider provider = new MyDataProvider(); // Need to instantiate an actual DataProvider
		Cache<Integer,Integer> cache = new LRUCache3<>(provider, 5);

		for (int i = 0; i < 1; i++){
			int t = cache.get(i); // throw the value away
		}
		assertEquals(cache.get(0), new Integer(0));
		assertEquals(provider.getNumFetches(), 1);
	}

	@Test
	/**
	 * Tests the cases where all get calls will be misses
	 */
	public void allButOneMisses(){
		MyDataProvider provider = new MyDataProvider(); // Need to instantiate an actual DataProvider
		Cache<Integer,Integer> cache = new LRUCache3<>(provider, 1);

		for (int i = 0; i < 100; i++){
			int t = cache.get(i); // throw the value away
		}

		assertEquals(cache.getNumMisses(), 100);
	}

	@Test
	/**
	 * Tests the case when all of the initial values have been replaced
	 * and that no values have been improperly overwritten
	 */
	public void replaceAllValues(){
		MyDataProvider provider = new MyDataProvider(); // Need to instantiate an actual DataProvider
		Cache<Integer,Integer> cache = new LRUCache3<>(provider, 1000);

		for (int i = 0; i < 1000; i++){ // fills the array
			int t = cache.get(i); // throw the value away
		}

		for (int i = 1000; i < 2000; i++){ // replaces all the values in the array
			int t = cache.get(i); // throw the value away
		}

		for (int i = 1000; i < 2000; i++){ // verifies that all values from the above loop have been placed in cache
			int t = cache.get(i); // throw the value away
		}

		assertEquals(cache.getNumMisses(), 2000);
	}

	@Test
	/**
	 * Checks that the time for constant eviction time
	 */
	public void testConstantTimeEviction(){
		List<Double> times = new ArrayList<>();

		for (int i = 1; i <= ITERATIONS; i++){
			MyDataProvider provider = new MyDataProvider(); // Need to instantiate an actual DataProvider
			Cache<Integer,Integer> cache = new LRUCache3<>(provider, i * 1000);

			for (int j = 0; j < i * 1000; j++){ // fill the cache with values
				int t = cache.get(j); // throw the value away
			}

			int total = 0;
			for (int k = 0; k < 10; k++){
				final double start = System.currentTimeMillis();
				for (int l = 0; l < 100000; l++){ // tries to run through 100 thousand values that are not in the cache
					cache.get(i * 1000 + l); // throw the value away
				}
				final double end = System.currentTimeMillis();
				total += end-start;
			}
			times.add(total / 10.0);
		}
		int total = 0;
		int lessThanTotal = 0;
		int equalsTotal = 0;
		for (int f = 0; f < times.size() - 1; f++){
			for (int s = f + 1; s < times.size(); s++){
				if (times.get(f) < times.get(s)){
					lessThanTotal++;
				} else if (times.get(f).equals(times.get(s))){
					equalsTotal++;
				}
				total++;
			}
		}
		System.out.println(times);
		double timeFraction = (lessThanTotal + (equalsTotal / 2.0)) / total;
		System.out.println(timeFraction);
		assertTrue(0.4 < timeFraction && timeFraction < 0.6);
	}

	@Test
	/**
	 * Checks that the time for constant getting time
	 */
	public void testConstantTimeGet(){
		List<Double> times = new ArrayList<>();

		for (int i = 1; i <= ITERATIONS; i++){
			MyDataProvider provider = new MyDataProvider(); // Need to instantiate an actual DataProvider
			Cache<Integer,Integer> cache = new LRUCache3<>(provider, i * 10000);

			int total = 0;
			for (int k = 0; k < 5; k++){
				double start = System.currentTimeMillis();
				for (int j = 0; j < 100000; j++){ // fill the cache with values

					int random = (int) (i * 10000 * Math.random()); // finds a random number between 0 and the capacity
					cache.get(random); // throw the value away
				}
				double end = System.currentTimeMillis();
				total += end-start;
			}
			times.add(total / 5.0);
		}
		System.out.println(times);
		int total = 0;
		int lessThanTotal = 0;
		int equalsTotal = 0;
		for (int f = 0; f < times.size() - 1; f++){
			for (int s = f + 1; s < times.size(); s++){
				if (times.get(f) < times.get(s)){ // TODO: PLEASE FIX
					lessThanTotal++;
				} else if (times.get(f).equals(times.get(s))){
					equalsTotal++;
				}
				total++;
			}
		}
		double timeFraction = (lessThanTotal + (equalsTotal / 2.0)) / total;
		System.out.println(timeFraction);
		assertTrue(0.4 < timeFraction && timeFraction < 0.6);
	}
}
