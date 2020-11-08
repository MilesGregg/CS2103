package Project2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HashMapTester {



    public static void main(String[] args) {
        Random rng = new Random();
        int[] times = new int[1000];
        for (int i = 100; i<100000; i += 100){
            Map<Integer, Node> map = new HashMap<>();
            for(int j = 0; j < i; j++) {
                map.put(j, new Node(j, j));
            }
            int[] rands = new int[10000000];
            for(int j = 0; j < 10000000; j++){
                rands[j] = rng.nextInt(i);
            }
            long start = System.nanoTime();
            for (int rand : rands) {
                map.get(rand);
            }
            long end = System.nanoTime();
            long elapsed = end - start;
            times[i/100] = (int) elapsed;
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
        System.out.println("GREATER: "+(double) greater/trials);
        System.out.println("EQUAL: "+(double) equal/trials);
        System.out.println((double)greater/trials + 0.5 * (double) equal/trials);
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
