import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test <tt>WordSearch3D</tt>.
 */
public class WordSearchGrader {
    private WordSearch3D _wordSearch;

    /**
     *  Verifies that search works correctly in a tiny grid that is effectively 2D.
     */
    @Test(timeout=60000)
    public void testSearchSimple () {
        // Note: this grid is 1x2x3 in size
        final char[][][] grid = new char[][][] { { { 'a', 'b', 'c' },
                { 'd', 'f', 'e' } } };
        final int[][] location = _wordSearch.search(grid, "be");
        assertNotNull(location);
        assertEquals(location[0][0], 0);
        assertEquals(location[0][1], 0);
        assertEquals(location[0][2], 1);
        assertEquals(location[1][0], 0);
        assertEquals(location[1][1], 1);
        assertEquals(location[1][2], 2);

        System.out.println("CS2103GRDR +2");
    }

    // Checks that search() finds the specified word in the specified grid and
    private void verifySearch (char[][][] grid, String[] words, int numPoints) {
        // Verify all words are found
        for (int i = 0; i < words.length; i++) {
            final int[][] location = _wordSearch.search(grid, words[i]);
            assertNotNull(location);
        }
        System.out.println("CS2103GRDR +" + numPoints);

        // Verify all words are found at correct place
        for (int i = 0; i < words.length; i++) {
            final int[][] location = _wordSearch.search(grid, words[i]);
            int[] letter = location[0];
            assertEquals(grid[letter[0]][letter[1]][letter[2]], words[i].charAt(0));
            letter = location[location.length - 1];
            assertEquals(grid[letter[0]][letter[1]][letter[2]], words[i].charAt(words[i].length() - 1));
        }
        System.out.println("CS2103GRDR +" + numPoints);
    }

    private char[][][] loadGrid (String filename) throws FileNotFoundException {
        final Scanner s = new Scanner(new File("grid.txt"));
        // First scan for the size of the grid
        final int sizeX = s.nextInt();
        final int sizeY = s.nextInt();
        final int sizeZ = s.nextInt();
        final char[][][] grid = new char[sizeX][sizeY][sizeZ];
        // Now scan for the characters in the grid
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    final String str = s.next();
                    grid[x][y][z] = str.charAt(0);
                }
            }
        }
        return grid;
    }

    private void testFruitHelper (String[] words) throws FileNotFoundException {
        final char[][][] grid = loadGrid("grid.txt");
        verifySearch(grid, words, 2);
    }

    @Test(timeout=60000)
    public void testFruitSearchSimple () throws FileNotFoundException {
        testFruitHelper(new String[] { "lime", "persimmon", "tangerine", "orange", "guava" });
    }

    @Test(timeout=360000)
    public void testMake1D () {
        final String[] words = new String[] { "java" };
        // Solution is either java or avaj
        final char[][][] grid = _wordSearch.make(words, 1, 1, 4);
        final char[] row = grid[0][0];
        assertTrue((row[0] == 'j' && row[1] == 'a' && row[2] == 'v' && row[3] == 'a') ||
                (row[3] == 'j' && row[2] == 'a' && row[1] == 'v' && row[0] == 'a'));

        System.out.println("CS2103GRDR +2");
    }

    @Test(timeout=60000)
    public void testMakeImpossible () {
        final String[] words = new String[] { "af", "ad", "def", "bc" };
        // def has to be in a row of its own.
        // bc must be on the other row.
        // The last remaining cell is an a, which cannot
        // be adjacent to both d and f.
        final char[][][] grid = _wordSearch.make(words, 2, 3, 1);
        assertNull(grid);

        System.out.println("CS2103GRDR +2");
    }

    @Test(timeout=60000)
    public void testMakeWithIntersection () {
        final String[] words = new String[] { "amc", "dmf", "gmi", "jml", "nmo", "pmr", "smu", "vmx", "yma", "zmq" };
        final char[][][] grid = _wordSearch.make(words, 3, 3, 3);
        assertNotNull(grid);

        System.out.println("CS2103GRDR +6");
    }

    @Test(timeout=60000)
    public void testRandom5 () {
        testOneRandomHelper(5);
    }

    @Test(timeout=60000)
    public void testRandom30 () {
        testOneRandomHelper(30);
    }

    public void testOneRandomHelper (int numWords) {
        final int NUM_PUZZLES = 5;
        final Random rng = new Random(0);
        // Difficulty (# words) increases for each test
        final int SIZE = 10;
        final int MAX_WORD_LEN = SIZE;
        final String[] words = new String[numWords];
        // Create some random words of random lengths with random characters
        for (int i = 0; i < numWords; i++) {
            final StringBuilder sb = new StringBuilder();
            final int wordLen = rng.nextInt(MAX_WORD_LEN - 2) + 2;  // >=2 characters
            for (int j = 0; j < wordLen; j++) {
                sb.append((char) ('a' + rng.nextInt(26)));
            }
            words[i] = sb.toString();
        }
        // Make puzzle
        final char[][][] grid = _wordSearch.make(words, SIZE, SIZE, SIZE);
        assertNotNull(grid);
        System.out.println("CS2103GRDR +2");

        // Solve puzzle and make sure we can find all thew ords
        verifySearch(grid, words, 1);
    }

    @Test(timeout=60000)
    public void testMakeGridSize () {
        final String[] words = new String[] { "at", "it", "ix", "ax" };
        final char[][][] grid = _wordSearch.make(words, 17, 11, 13);
        assertEquals(grid.length, 17);
        for (int x = 0; x < 2; x++) {
            assertEquals(grid[x].length, 11);
            for (int y = 0; y < 2; y++) {
                assertEquals(grid[x][y].length, 13);
            }
        }

        System.out.println("CS2103GRDR +2");
    }

    @Before
    public void setUp () {
        _wordSearch = new WordSearch3D();
    }
}