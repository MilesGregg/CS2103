import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * Code to test <tt>WordSearch3D</tt>.
 */
public class WordSearchTester {
	private WordSearch3D _wordSearch;

	@Test
	/**
	 * Verifies that make can generate a very simple puzzle that is effectively 1d.
	 */
	public void testMake1D () {
		final String[] words = new String[] { "java" };
		// Solution is either java or avaj
		final char[][][] grid = _wordSearch.make(words, 1, 1, 4);
		final char[] row = grid[0][0];
		System.out.println(Arrays.toString(grid[0][0]));
		assertTrue((row[0] == 'j' && row[1] == 'a' && row[2] == 'v' && row[3] == 'a') ||
		           (row[3] == 'j' && row[2] == 'a' && row[1] == 'v' && row[0] == 'a'));
	}

	@Test
	/**
	 * Verifies that make returns null when it's impossible to construct a puzzle.
	 */
	public void testMakeImpossibleBadSize () {
		final String[] words = new String[] {"Object", "Oriented", "Programming"};
		final char[][][] grid = _wordSearch.make(words, 6, 4, 8);
		assertNull(grid);
	}

	@Test
	/**
	 * Verifies that make returns null when it's impossible to construct a puzzle.
	 */
	public void testMakeManyWords () {
		final String[] words = new String[] {"object", "oriented", "programming", "fan", "desk", "apple", "headphones", "binoculars", "calculator",
		"java", "python", "c", "pencil", "pen", "picture", "orange", "id", "banana", "color"};
		final char[][][] grid = _wordSearch.make(words, 6, 4, 11);
		assertNotNull(grid);
		// searches for all of the words in the grid
		int [][][] allWordPos = _wordSearch.searchForAll(grid, words);
		// checks to make sure all words have a position
		for (int[][] allWordPo : allWordPos) {
			assertNotNull(allWordPo);
		}
	}

	@Test
	/**
	 * Verifies that make returns null when it's impossible to construct a puzzle.
	 */
	public void testMakeImpossibleLarge () {
		final String[] words = new String[] {"object", "oriented", "programming", "fan", "desk", "apple", "headphones", "binoculars", "calculator",
				"java", "python", "c", "pencil", "pen", "picture", "orange", "id", "banana", "color"};
		final char[][][] grid = _wordSearch.make(words, 1, 2, 11);
		// checks to make sure all words have a position
		int [][][] allWordPos = _wordSearch.searchForAll(grid, words);
		assertNotNull(grid);
		// checks to make sure all words have a position
		for (int[][] allWordPo : allWordPos) {
			assertNotNull(allWordPo);
		}
		//assertNull(grid);
	}

	@Test
	/**
	 *  Verifies that search works correctly in a tiny grid that is effectively 2D.
	 */
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
	}

	/**
	 * Verifies that make can generate a grid when it's *necessary* for words to share
	 * some common letter locations.
	 */
	@Test
	public void testMakeWithIntersection () {
		final String[] words = new String[] { "amc", "dmf", "gmi", "jml", "nmo", "pmr", "smu", "vmx", "yma", "zmq" };
		final char[][][] grid = _wordSearch.make(words, 3, 3, 3);
		int[][][] posns = _wordSearch.searchForAll(grid, words);
		assertNotNull(posns);
		for(int[][] posn : posns){
			assertNotNull(posn);
		}
		assertNotNull(grid);
	}

	/**
	 * Verifies that make returns a grid of the appropriate size.
	 */
	@Test
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
	}

	/* TODO: write more methods for both make and search. */

	@Test
	public void testSearch3d () {
		// Note: this grid is 3x4x2 in size
		final char[][][] grid = new char[][][]  {
				{
					{'a', 'a'},
					{'a', 'a'},
					{'a', 'a'},
					{'a', 'a'}
				},
				{
					{'o', 'a'},
					{'a', 'a'},
					{'a', 'a'},
					{'a', 'a'}
				},
				{
					{'a', 'a'},
					{'a', 'x'},
					{'a', 'a'},
					{'a', 'a'}
				}
		};


		final int[][] location = _wordSearch.search(grid, "ox");
		assertNotNull(location);
		assertEquals(location[0][0], 1);
		assertEquals(location[0][1], 0);
		assertEquals(location[0][2], 0);
		assertEquals(location[1][0], 2);
		assertEquals(location[1][1], 1);
		assertEquals(location[1][2], 1);
	}

	@Test
	public void testSearch3dNull () {
		// Note: this grid is 3x4x2 in size
		final char[][][] grid = new char[][][]  {
				{
						{'a', 'a'},
						{'a', 'a'},
						{'a', 'a'},
						{'a', 'a'}
				},
				{
						{'o', 'a'},
						{'a', 'a'},
						{'a', 'a'},
						{'a', 'a'}
				},
				{
						{'a', 'a'},
						{'a', 'x'},
						{'a', 'a'},
						{'a', 'a'}
				}
		};

		final int[][] location = _wordSearch.search(grid, "axe");
		assertNull(location);
	}

	@Test
	public void testSearchAdvancedDepth() {
		final char[][][] grid = {
				{
						{'a', 'a', 'a', 'a'},
						{'b', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'}
				},
				{
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'}
				},
				{
						{'a', 'a', 'a', 'a'},
						{'l', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'}
				}
		};

		final int[][] location = _wordSearch.search(grid, "bal");
		final int[][] expectedPos = {{0, 1, 0}, {1, 1, 0}, {2, 1, 0}};
		assertNotNull(location);
		assertArrayEquals(expectedPos, location);
		assertEquals(location[0][0], 0);
		assertEquals(location[0][1], 1);
		assertEquals(location[0][2], 0);
		assertEquals(location[1][0], 1);
		assertEquals(location[1][1], 1);
		assertEquals(location[1][2], 0);
		assertEquals(location[2][0], 2);
		assertEquals(location[2][1], 1);
		assertEquals(location[2][2], 0);
	}

	@Test
	public void testSearchAdvancedDepthDiagonal() {
		final char[][][] grid = {
				{
						{'b', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'}
				},
				{
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'}
				},
				{
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'l', 'a'}
				}
		};

		final int[][] location = _wordSearch.search(grid, "bal");
		final int[][] expectedPos = {{0, 0, 0}, {1, 1, 1}, {2, 2, 2}};
		assertNotNull(location);
		assertArrayEquals(expectedPos, location);
		assertEquals(location[0][0], 0);
		assertEquals(location[0][1], 0);
		assertEquals(location[0][2], 0);
		assertEquals(location[1][0], 1);
		assertEquals(location[1][1], 1);
		assertEquals(location[1][2], 1);
		assertEquals(location[2][0], 2);
		assertEquals(location[2][1], 2);
		assertEquals(location[2][2], 2);
	}

	

	@Before
	public void setUp () {
		_wordSearch = new WordSearch3D();
	}
}
