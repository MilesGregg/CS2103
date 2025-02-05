import java.util.Random;
import java.util.*;
import java.io.*;

/**
 * Implements a 3-d word search puzzle program.
 */
public class WordSearch3D {
	// a list of all possible directions we can move in the 3d grid
	private final static ArrayList<int[]> vectors = new ArrayList<>();

	public WordSearch3D () {
		// initialize vectors
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					// excludes the vector <0 0 0> because it has no direction
					if (i == 0 && j == 0 && k == 0) continue;
					vectors.add(new int[] {i, j, k});
				}
			}
		}
	}

	/**
	 * Searches for all the words in the specified list in the specified grid.
	 * You should not need to modify this method.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param words the words to search for
	 * @return a list of lists of locations of the letters in the words
	 */
	public int[][][] searchForAll (char[][][] grid, String[] words) {
		final int[][][] locations = new int[words.length][][];
		for (int i = 0; i < words.length; i++) {
			locations[i] = search(grid, words[i]);
		}
		return locations;
	}

	/**
	 * Given a starting position in the grid and a vector, checks if you can obtain the desired word
	 * by starting at the start point and moving in the direction of the vector
	 * @param grid the grid that is being searched
	 * @param word the word that is being searched for
	 * @param vector the vector signaling which direction from the starting point to look
	 * @param i the x-component of the current position in the grid
	 * @param j the y-component of the current position in the grid
	 * @param k the z-component of the current position in the grid
	 * @return a list of the coordinates for each point in the word if the word can be found with
	 * the initial conditions specified, null otherwise
	 */
	private int[][] checkVector (char[][][] grid, String word, int[] vector, int i, int j, int k){
		int xdir = vector[0];
		int ydir = vector[1];
		int zdir = vector[2];
		StringBuilder newWord = new StringBuilder();
		// generates the word that would be formed if we go in that vector direction and the starting (x, y, z) position
		for(int n = 0; n < word.length(); n++){
			char ch = grid[i + xdir * n][j + ydir * n][k + zdir * n];
			newWord.append(ch);
		}
		// checks if the new word matches the word we are searching for
		if (newWord.toString().equals(word)) {
			int[][] out = new int[word.length()][3];
			for(int c = 0; c < word.length(); c++){
				out[c] = new int[] {i + xdir * c, j+ydir * c, k+zdir * c};
			}
			// return a list of (x, y, z) positions for each character in the word
			return out;
		}
		// if we can't find the word then return null
		return null;
	}

	/**
	 * Given a starting position in the grid, checks if a "word" of the same length as the word that
	 * is being searched for can fit within the grid without going out of bounds
	 * @param vector the vector signaling which direction from the starting point to look
	 * @param grid the grid that is being searched
	 * @param word the word that is being searched for
	 * @param i the x-component of the current position in the grid
	 * @param j the y-component of the current position in the grid
	 * @param k the z-component of the current position in the grid
	 * @return true if the "word" cannot be searched for or placed without going out of bounds, false otherwise
	 */
	private boolean invalid (int[] vector, char[][][] grid, String word, int i, int j, int k){
		int xdir = vector[0];
		int ydir = vector[1];
		int zdir = vector[2];
		int i1 = i + xdir * (word.length() - 1);
		int j1 = j + ydir * (word.length() - 1);
		int k1 = k + zdir * (word.length() - 1);
		// makes sure all ending positions are in the grid
		return (i1 < 0 || i1 >= grid.length) ||
				(j1 < 0 || j1 >= grid[0].length) ||
				(k1 < 0 || k1 >= grid[0][0].length);
	}

	/**
	 * Searches for the specified word in the specified grid.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param word the word to search for
	 * @return If the grid contains the
	 * word, then the method returns a list of the (3-d) locations of its letters; if not,
	 */
	public int[][] search (char[][][] grid, String word) {
		if (grid == null || word == null) return null;
		// loops every starting position in the 3d grid
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				for (int k = 0; k < grid[0][0].length; k++) {
					// loops every vector direction at each starting position
					for (int[] item : vectors) {
						// checks if the vector goes out bounds
						if (invalid(item, grid, word, i, j, k)) continue;
						// checks the word in the grid makes the one we are looking for
						int[][] out = checkVector(grid, word, item, i, j, k);
						// output position if the word matches
						if (out != null) return out;
					}
				}
			}
		}
		// return null if it can't find the word
		return null;
	}

	/**
	 * Inserts word into grid given start pos (x, y, z) and goes in vector direction.
	 * @param grid - 3d grid characters
	 * @param vector - vector direction where the word should go
	 * @param position - starting x, y, z direction for the work
	 * @param word - word that needs to be inserted
	 */
	private void insert (char[][][] grid, int[] vector, int[] position, String word) {
		for(int i = 0; i < word.length(); i++) {
			// inserts each letter into their certian position of the 3d grid
			grid[position[0] + i*vector[0]][position[1] + i*vector[1]][position[2] + i*vector[2]] = word.charAt(i);
		}
	}

	/**
	 * Add random chars to the rest of the grid once it is filled with words
	 * @param grid - 3d grid of characters
	 */
	private void addRandom (char[][][] grid) {
		final Random rng = new Random();

		for (int i1 = 0; i1 < grid.length; i1++) {
			for (int j = 0; j < grid[0].length; j++) {
				for (int k1 = 0; k1 < grid[0][0].length; k1++) {
					// checks to make sure there is no letter there
					if((!Character.isLetter(grid[i1][j][k1])))
						// inserts a random character into that grid 3d position
						grid[i1][j][k1] =  (char) (rng.nextInt(26) + 'a');
				}
			}
		}
	}

	/**
	 * checks to see if inserting the word will overwrite any other words
	 * @param word - current word
	 * @param grid - 3d char grid
	 * @param position - starting x, y, z position to place the word at
	 * @param randomVector - random direction to go
	 * @return - if we can place the word
	 */
	private boolean possible(String word, char[][][] grid, int[] position, int[] randomVector) {
		for (int t = 0; t < word.length(); t++) {
			char ch = grid[position[0] + t * randomVector[0]][position[1] + t * randomVector[1]][position[2] + t * randomVector[2]];
			if (Character.isLetter(ch) && ch != word.charAt(t)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tries to create a word search puzzle of the specified size with the specified
	 * list of words.
	 * @param words the list of words to embed in the grid
	 * @param sizeX size of the grid along first dimension
	 * @param sizeY size of the grid along second dimension
	 * @param sizeZ size of the grid along third dimension
	 * @return a 3-d char array if successful that contains all the words, or <tt>null</tt> if
	 * no satisfying grid could be found.
	 */
	public char[][][] make (String[] words, int sizeX, int sizeY, int sizeZ) {
		char[][][] grid = new char[sizeX][sizeY][sizeZ];
		final Random rng = new Random();
		// if the list of words is null, just initialize and return a random grid
		if (words == null || (sizeX == 0 || sizeY == 0 || sizeZ == 0)) {
			addRandom(grid);
			return grid;
		}

		for (int _counter = 0; _counter < 1000; _counter++) {
			grid = new char[sizeX][sizeY][sizeZ];
			for (int i = 0; i < words.length; i++) {
				int wordLoop;
				for (wordLoop = 0; wordLoop < 1000; wordLoop++) {
					// generates a random start position for the word
					int[] randomPosition = {rng.nextInt(sizeX), rng.nextInt(sizeY), rng.nextInt(sizeZ)};
					// generates a random vector direction to go
					int[] randomVector = vectors.get(rng.nextInt(26));

					// checks to make sure the word will not go out of bounds if placed at that position and direction
					if (invalid(randomVector, grid, words[i], randomPosition[0], randomPosition[1], randomPosition[2])) continue;

					// checks to make sure it won't overwrite any existing words
					if (possible(words[i], grid, randomPosition, randomVector)) {
						//if the word can be placed than place that word
						insert(grid, randomVector, randomPosition, words[i]);

						if (i == words.length - 1) {
							// add random characters in the 3d grid
							addRandom(grid);
							// then return the grid
							return grid;
						} else break;
					}
				}
				// break out of word for loop if we did 1000 checks for trying to place the word
				if (wordLoop == 1000) break;
			}
		}
		// return null if the grid couldn't be made
		return null;
	}

	/**
	 * Exports to a file the list of lists of 3-d coordinates.
	 * You should not need to modify this method.
	 * @param locations a list (for all the words) of lists (for the letters of each word) of 3-d coordinates.
	 * @param filename what to name the exported file.
	 */
	public static void exportLocations (int[][][] locations, String filename) {
		// First determine how many non-null locations we have
		int numLocations = 0;
		for (int[][] location : locations) {
			if (location != null) {
				numLocations++;
			}
		}

		try (final PrintWriter pw = new PrintWriter(filename)) {
			pw.print(numLocations);  // number of words
			pw.print('\n');
			for (int i = 0; i < locations.length; i++) {
				if (locations[i] != null) {
					pw.print(locations[i].length);  // number of characters in the word
					pw.print('\n');
					for (int j = 0; j < locations[i].length; j++) {
						for (int k = 0; k < 3; k++) {  // 3-d coordinates
							pw.print(locations[i][j][k]);
							pw.print(' ');
						}
					}
					pw.print('\n');
				}
			}
			pw.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	/**
	 * Exports to a file the contents of a 3-d grid.
	 * You should not need to modify this method.
	 * @param grid a 3-d grid of characters
	 * @param filename what to name the exported file.
	 */
	public static void exportGrid (char[][][] grid, String filename) {
		try (final PrintWriter pw = new PrintWriter(filename)) {
			pw.print(grid.length);  // height
			pw.print(' ');
			pw.print(grid[0].length);  // width
			pw.print(' ');
			pw.print(grid[0][0].length);  // depth
			pw.print('\n');
			for (int x = 0; x < grid.length; x++) {
				for (int y = 0; y < grid[0].length; y++) {
					for (int z = 0; z < grid[0][0].length; z++) {
						pw.print(grid[x][y][z]);
						pw.print(' ');
					}
				}
				pw.print('\n');
			}
			pw.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	/**
	 * Creates a 3-d word search puzzle with some nicely chosen fruits and vegetables,
	 * and then exports the resulting puzzle and its solution to grid.txt and locations.txt
	 * files.
	 */
	public static void main (String[] args) {
		final WordSearch3D wordSearch = new WordSearch3D();
		final String[] words = new String[] { "apple", "orange", "pear", "peach", "durian", "lemon", "lime", "jackfruit", "plum", "grape", "apricot", "blueberry", "tangerine", "coconut", "mango", "lychee", "guava", "strawberry", "kiwi", "kumquat", "persimmon", "papaya", "longan", "eggplant", "cucumber", "tomato", "zucchini", "olive", "pea", "pumpkin", "cherry", "date", "nectarine", "breadfruit", "sapodilla", "rowan", "quince", "toyon", "sorb", "medlar" };
		final int xSize = 10, ySize = 10, zSize = 10;
		final char[][][] grid = wordSearch.make(words, xSize, ySize, zSize);
		exportGrid(grid, "grid.txt");

		final int[][][] locations = wordSearch.searchForAll(grid, words);
		exportLocations(locations, "locations.txt");
	}
}
