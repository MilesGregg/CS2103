package Project2;

/**
 * CS 2103 2019 B-term (Whitehill)
 * A cache that associates keys with values.
 */
interface Cache<T, U> extends DataProvider<T, U> {
	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	int getNumMisses ();
}

/*
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
 */