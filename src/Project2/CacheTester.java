package Project2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTester {

	private static class Database implements DataProvider<Integer, String> {

		@Override
		public String get(Integer key) {
			return String.valueOf(key);
		}
	}

	private static class Database2 implements DataProvider<Double, int[]> {


		@Override
		public int[] get(Double key) {
			return new int[] {(int) (double) key, (int) (double) key + 1};
		}
	}
	@Test
	public void leastRecentlyUsedIsCorrect () {
		Database provider = new Database();


		// Need to instantiate an actual DataProvider
		Cache<Integer,String> cache = new LRUCache2<>(provider, 5);
		String x = cache.get(4);
		assertEquals(x, "4");
		String y = cache.get(15);
		assertEquals(y, "15");
		String z = cache.get(-4);
		assertEquals(z, "-4");
		assertEquals(cache.getNumMisses(), 3);
		String a = cache.get(-4);
		assertEquals(cache.getNumMisses(), 3);
		String b = cache.get(23);
		assertEquals(b, "23");
		String c = cache.get(1);
		assertEquals(c, "1");
		assertEquals(cache.getNumMisses(), 5);
		String d = cache.get(15);
		assertEquals(cache.getNumMisses(), 5);
		String f = cache.get(7);
		assertEquals(cache.getNumMisses(), 6);
		String e = cache.get(4);
		assertEquals(cache.getNumMisses(), 7);
	}

	@Test
	public void testTimeComplexity () {
		double sum = 0;
		for (int counter = 0; counter < 10; counter++) {
			Database provider = new Database();
			long[] times = new long[100];
			for (int k = 1; k < 101; k++) {
				Cache<Integer, String> cache1 = new LRUCache2<>(provider, 100 * k);
				for (int q = 0; q < 100 * k; q++)
					cache1.get(q);
				final long start1 = System.currentTimeMillis();
				for (int j = 0; j < 1000000; j++)
					cache1.get((int) (100 * k));
				final long end1 = System.currentTimeMillis();
				final long timeDiff1 = end1 - start1;
				times[k - 1] = timeDiff1;
			}
			int greater = 0;
			int equal = 0;
			int trials = 0;
			for (int i = 5; i < times.length; i++)
				for (int j = i + 1; j < times.length; j++) {
					trials++;
					if (times[j] > times[i])
						greater++;
					else if (times[j] == times[i])
						equal++;
				}
			double greaterFraction = (double) greater / trials;
			double equalFraction = (double) equal / trials;
				sum += greaterFraction + equalFraction / 2;
		}
		assertTrue(sum / 10 <= 0.6 && sum / 10 >= 0.4);
	}




	@Test
	public void testLRUWithLargeNumbers(){
		Database provider = new Database();
		// Need to instantiate an actual DataProvider
		Cache<Integer,String> cache = new LRUCache2<>(provider, 500);
		// Every get() operation here is a miss, so the number of misses should increase by 1 each time
		for(int i = 0; i < 750; i++){
			cache.get(i);
			assertEquals(cache.getNumMisses(), i+1);
		}
		// every get() operation here is a hit, so the number of misses should not change
		for(int j = 499; j >= 250; j--){
			cache.get(j);
			assertEquals(cache.getNumMisses(), 750);
		}
		// every get() operation here is a miss again, so the number of misses should increase by 1 each time
		for(int k = 249; k >= 0; k--){
			cache.get(k);
			assertEquals(cache.getNumMisses(), 750 + 250 - k);
		}
	}

	@Test
	public void testLRUWithLargeNumbers2(){
		Database2 provider = new Database2();
		// Need to instantiate an actual DataProvider
		Cache<Double, int[]> cache = new LRUCache2<>(provider, 500);
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

}
