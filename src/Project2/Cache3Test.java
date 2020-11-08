package Project2;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class Cache3Test {

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
        int maincount = 0;
        double[] arr = new double[10];
        for (int outcounter = 0; outcounter < 1; outcounter++) {
            double sum = 0;
            for (int counter = 0; counter < 1; counter++) {
                Database provider = new Database();
                long[] times = new long[300];
                for (int k = 1; k < 301; k++) {
                    Cache<Integer, String> cache1 = new LRUCache3<>(provider, 100 * k);
                    for (int q = 0; q < 100 * k; q++)
                        cache1.get(q);
                    final long start1 = System.currentTimeMillis();
                    for (int j = 0; j < 800000; j++)
                        cache1.get((int) (100 * k * j / 800000.0));
                    final long end1 = System.currentTimeMillis();
                    final long timeDiff1 = end1 - start1;
                   // System.out.println(timeDiff1);
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
                arr[counter] = (greaterFraction + equalFraction/2);
                long min = Arrays.stream(times).min().getAsLong();
                long max = Arrays.stream(times).max().getAsLong();
                System.out.println(Arrays.toString(times));
                System.out.println(greaterFraction);
                System.out.println(equalFraction/2);
                System.out.println("MAX: "+max);
                System.out.println("MIN: "+min);
            }
            System.out.println(sum);
            System.out.println(Arrays.toString(arr));
            assertTrue(sum <= 0.6 && sum >= 0.4);

        }
        System.out.println(maincount);

    }
}
