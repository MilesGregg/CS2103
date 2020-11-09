package Project2;

public class Testing2 {
    static int countNumCommonLetters(String word1, String word2) {
        int[] count1 = new int[26];
        int[] count2 = new int[26];

        int count = 0;

        for (int i = 0; i < word1.length(); i++) {
            count1[word1.charAt(i) - 'a']++;
        }

        for (int i = 0; i < word2.length(); i++) {
            count2[word2.charAt(i) - 'a']++;
        }

        for (int i = 0; i < 26; i++) {
            count += (Math.min(count1[i], count2[i]));
        }

        return count;
    }
}
