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
		int totalCount = 0;
		for(int aa = 0; aa < 100; aa++) {
			Random rand = new Random();
			for (int outcounter = 0; outcounter < 1; outcounter++) {
				double sum = 0;
				for (int counter = 0; counter < 10; counter++) {
					Database provider = new Database();
					long[] times = new long[100];
					for (int k = 1; k <= 100; k++) {
						Cache<Integer, String> cache1 = new LRUCache2<>(provider, 1000 * k);
						for (int q = 0; q < 1000 * k; q++)
							cache1.get(q);
						int[] rands = new int[100000];
						for (int j = 0; j < rands.length; j++) {
							rands[j] = rand.nextInt(1000 * k);
						}
						final long start1 = System.currentTimeMillis();
						for (int j = 0; j < 1000; j++)
							cache1.get(rands[j]);
						final long end1 = System.currentTimeMillis();
						final long timeDiff1 = end1 - start1;
						times[k - 1] = timeDiff1;
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
				if(sum / 10 <= 0.6 && sum / 10 >= 0.4) {
					System.out.println("SUCCESS: "+sum/10);
					totalCount++;
				}
			}
		}
		System.out.println(totalCount);
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


	@Test
	public void testTimeComplexity2 () {
		for (int counter = 0; counter < 20; counter++) {
			final int sizeMul = 1000;

			DataProvider<Integer, String> provider = new Database();

			final int trials = 100;
			double[] averageTimeCosts = new double[trials];

			for (int i = 1; i <= trials; i++) {
				double averageTime = getAverageTimeCost(provider, sizeMul * i);
			/*if (averageTime < 0.45 || averageTime == 0.0) {
				averageTimeCosts[i - 1] = averageTime;
			}*/
				averageTimeCosts[i - 1] = averageTime;
			}

			//System.out.println(Arrays.toString(averageTimeCosts));

			int greater = 0;
			int equal = 0;
			int count = 0;
			for (int i = 0; i < averageTimeCosts.length; i++) {
				for (int j = i + 1; j < averageTimeCosts.length; j++) {
					count++;
					if (averageTimeCosts[j] > averageTimeCosts[i])
						greater++;
					else if (averageTimeCosts[j] == averageTimeCosts[i])
						equal++;
				}
			}
			double greaterFraction = (double) greater / count;
			System.out.println(greaterFraction);

			/*int count = 0, greater = 0, equal = 0;

			for (int i = 0; i < averageTimeCosts.length; i += 2) {
				count++;
				if (averageTimeCosts[i + 1] > averageTimeCosts[i]) {
					greater++;
				}
			}

			System.out.println((double) greater / count);*/
		}
	}
//		//System.out.println(total);
//		System.out.println(Arrays.toString(averageTimeCosts));
//		for (int i = 0; i < averageTimeCosts.length; i++) {
//			for (int j = i + 1; j < averageTimeCosts.length; j++) {
//				total++;
//				if (averageTimeCosts[j] > averageTimeCosts[i])
//					greater++;
//				else if (averageTimeCosts[j] == averageTimeCosts[i])
//					equal++;
//			}
//		}
//		double greaterFraction = (double) greater / total;
//		double equalFraction = (double) equal / total;
//		System.out.println(greaterFraction);
//		System.out.println(greaterFraction + equalFraction / 2);
//		assertTrue(greaterFraction + equalFraction / 2 > 0.4 && greaterFraction + equalFraction / 2 < 0.6);
//
//	}
//
	public double getAverageTimeCost (DataProvider<Integer, String> provider, int capacity) {
		Cache<Integer, String> chache = new LRUCache3<>(provider, capacity);

		final int trials = 100;
		final int range = 100000;

		for(int j = 0; j < capacity; j++){
			chache.get(j);
		}

		long totalTime = 0;
		Random rand = new Random();
		for (int i = 0; i < trials; i++) {
			final long start = System.nanoTime();
			for (int j = 0; j < 100; j++) {
				//rand.nextInt(range);
				chache.get(i);
			}
			final long end = System.nanoTime();
			totalTime += (end - start);
		}

		return (double) totalTime/trials;
	}


}
