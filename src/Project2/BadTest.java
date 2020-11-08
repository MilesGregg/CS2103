package Project2;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class BadTest {

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
    public void testTimeComplexity () {
        double[] arr = new double[10];
        Random rand = new Random();
        for (int outcounter = 0; outcounter < 1; outcounter++) {
            double sum = 0;
            for (int counter = 0; counter < 20; counter++) {
                Database provider = new Database();
                long[] times = new long[100];
                for (int k = 1; k <= 100; k++) {
                    Cache<Integer, String> cache1 = new LRUCache2<>(provider, 100 * k);
                    for (int q = 0; q < 100 * k; q++)
                        cache1.get(q);
                    int[] rands = new int[1000000];
                    for(int j = 0; j < rands.length; j++){
                        rands[j] = rand.nextInt(100);
                    }
                    final long start1 = System.currentTimeMillis();
                    for (int j = 0; j < 1000000; j++)
                        cache1.get(rands[j]);
                    final long end1 = System.currentTimeMillis();
                    final long timeDiff1 = end1 - start1;
                    times[k - 1] = timeDiff1;
                }
                int greater = 0;
                int equal = 0;
                int trials = 0;
                for (int i = 0; i < times.length; i++)
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
                arr[outcounter] = (greaterFraction + equalFraction/2);
                long min = Arrays.stream(times).min().getAsLong();
                long max = Arrays.stream(times).max().getAsLong();
                System.out.println(Arrays.toString(times));
//				System.out.println(Arrays.toString(times));
//				System.out.println("greater: " + greaterFraction);
//				System.out.println(equalFraction/2);
//				System.out.println("MAX: "+max);
//				System.out.println("MIN: "+min);
            }
            //System.out.println(sum);
            //System.out.println(Arrays.toString(arr));
            System.out.println("FINAL: "+sum/20);
            assertTrue(sum/20 <= 0.6 && sum/20 >= 0.4);

        }
    }
}
