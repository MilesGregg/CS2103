package Project2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HashMapTester {



    public static void main(String[] args) {
        long startBig = System.currentTimeMillis();
        Random rng = new Random();
        long[] times = new long[100];
        for (int i = 100; i<100000; i += 1000){
            Map<Integer, Node> map = new HashMap<>(i*1000);
            for(int j = 0; j < i; j++) {
                // populates the hashmap with keys from 0 to i
                map.put(j, new Node(j, j));
            }
            int[] rands = new int[10000000];
            for(int j = 0; j < 10000000; j++){
                // creates an array of 10000000 random numbers between 0 and i
                rands[j] = rng.nextInt(i);
            }
            long start = System.currentTimeMillis();
            for (int rand : rands) {
                // runs 10 million get() operations using keys that are in the map
                Node u = map.get(rand);
                if(u == null) throw new RuntimeException();
            }
            // calculates and stores the elapsed time
            long end = System.currentTimeMillis();
            long elapsed = end - start;
            times[(i-100)/1000] = elapsed;
        }
        System.out.println(Arrays.toString(times));
        int greater = 0, equal = 0, trials = 0;
        for(int a = 0; a < times.length; a++){
            for(int b = a+1; b < times.length; b++){
                trials ++;
                if(times[b]>times[a])
                    greater ++;
                else if(times[b]==times[a])
                    equal++;
            }
        }
        long endBig = System.currentTimeMillis();
        System.out.println("GREATER: "+(double)greater/trials);
        System.out.println("EQUAL: "+equal/trials);
        System.out.println((double)greater/trials + 0.5 * (double) equal/trials);
        System.out.println("TIME: "+(endBig-startBig));
    }


    private static class Node {
        int key;
        int value;


        private Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
