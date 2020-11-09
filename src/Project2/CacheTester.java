package Project2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

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
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
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
		Random rand = new Random();
		double sum = 0;
		for (int counter = 0; counter < 10; counter++) {
			Database provider = new Database();
			long[] times = new long[25];
			for (int k = 20; k <= 500; k+=20) {
				Cache<Integer, String> cache1 = new LRUCache<>(provider, 1000 * k);
				for (int q = 0; q < 1000 * k; q++)
					cache1.get(q);
				int[] rands = new int[100000];
				for (int j = 0; j < rands.length; j++) {
					rands[j] = rand.nextInt(1000 * k);
				}
				final long start1 = System.currentTimeMillis();
				for (int i : rands)
					cache1.get(i);
				final long end1 = System.currentTimeMillis();
				final long timeDiff1 = end1 - start1;
				times[(k - 20)/20] = timeDiff1;
			}
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
			double greaterFraction = (double) greater / trials;
			double equalFraction = (double) equal / trials;
			sum += greaterFraction + equalFraction / 2;
		}
		System.out.println(sum/10);
		assertTrue(sum / 10 <= 0.6 && sum / 10 >= 0.4);
	}




	@Test
	public void testLRUWithLargeNumbers(){
		Database provider = new Database();
		// Need to instantiate an actual DataProvider
		Cache<Integer,String> cache = new LRUCache<>(provider, 500);
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
	public void testEviction(){
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
	public void testGetLRU(){
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
	public void testGetMRU(){
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
	public void testProvider() {
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
