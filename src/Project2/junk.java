package Project2;

import java.util.Random;

public class junk {
    public static void main(String[] args) {
        Random random = new Random();
        double avgdiff = 0;
        double avg1 = 0;
        for(int j = 0; j < 100; j ++) {
            long time1 = System.currentTimeMillis();
            for (int i = 1; i < 10000000; i++) {
                int k = (int) (100*i*Math.random());
            }
            long time2 = System.currentTimeMillis();
            avg1 += (time2 - time1);
        }
        System.out.println(avg1);

        double avg2 = 0;
        for(int j = 0; j < 100; j++) {
            long time1 = System.currentTimeMillis();
            for (int i = 1; i < 10000000; i++) {
                int k = (int) (i*Math.random());
            }
            long time2 = System.currentTimeMillis();
            avg2 += (time2 - time1);
        }
        System.out.println(avg2);

    }
}
