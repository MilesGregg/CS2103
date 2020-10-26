import java.util.Random;
import java.util.*;
import java.io.*;

/**
 * Implements a 3-d word search puzzle program.
 */
public class WordSearch3D {
	public WordSearch3D () {
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
	 * Searches for the specified word in the specified grid.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param word the word to search for
	 * @return If the grid contains the
	 * word, then the method returns a list of the (3-d) locations of its letters; if not,
	 */



	/**
	 * Creates a list of 3-dimensional vectors where each component of each vector is
	 * between -1 and 1, and all 3 components cannot be zero (total of 26 vectors)
	 * @return the list of vectors
	 */
	private static ArrayList<int[]> makeVectors(){
		ArrayList<int[]> o = new ArrayList<>();
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				for(int k = -1; k <= 1; k++){
					if(i == 0 && j == 0 && k == 0) continue; // excludes the vector <0 0 0> because it has no direction
					o.add(new int[] {i, j, k});
				}
			}
		}
		return o;
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
	private int[][] checkVector(char[][][] grid, String word, int[] vector, int i, int j, int k){
		int xdir = vector[0];
		int ydir = vector[1];
		int zdir = vector[2];
		StringBuilder newWord = new StringBuilder();
		for(int n = 0; n < word.length(); n++){
			char ch = grid[i + xdir * n][j + ydir * n][k + zdir * n];
			newWord.append(ch);
		}

		if (newWord.toString().equals(word)) {
			int[][] out = new int[word.length()][3];
			for(int c = 0; c < word.length(); c++){
				out[c] = new int[] {i + xdir * c, j+ydir * c, k+zdir * c};
			}
			return out;
		}
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
	 * @return true if the "word" can be searched for without going out of bounds, false otherwise
	 */
	private boolean valid(int[] vector, char[][][] grid, String word, int i, int j, int k){
		int xdir = vector[0];
		int ydir = vector[1];
		int zdir = vector[2];
		int i1 = i + xdir * (word.length() - 1);
		int j1 = j + ydir * (word.length() - 1);
		int k1 = k + zdir * (word.length() - 1);
		return !((i1 < 0 || i1 >= grid.length) ||
				(j1 < 0 || j1 >= grid[0].length) ||
				(k1 < 0 || k1 >= grid[0][0].length));
	}

	/**
	 * Searches for the specified word in the specified grid.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param word the word to search for
	 * @return If the grid contains the
	 * word, then the method returns a list of the (3-d) locations of its letters; if not,
	 */
	public int[][] search (char[][][] grid, String word) {
		ArrayList<int[]> vectors = makeVectors();
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[0].length; j++){
				for(int k = 0; k < grid[0][0].length; k++){
					for(int[] item : vectors){
						if(!valid(item, grid, word, i, j, k)) continue;
						int[][] out = checkVector(grid, word, item, i, j, k);
						if(out != null) return out;
					}
				}
			}
		}
		return null;
	}

	private void insert(char[][][] grid, int[] vector, int startX, int startY, int startZ, String word){
		for(int i = 0; i < word.length(); i++){
			//grid[startX + vector[0]*i][startY + vector[1]*i][startZ + vector[2]*i] = word.charAt(i);
			grid[startZ + i*vector[0]]
					[startY + i*vector[1]]
					[startX + i*vector[2]] = word.charAt(i);
		}
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
		// TODO: implement me

		int x = sizeX;
		int y = sizeY;
		int z = sizeZ;
		sizeZ = x;
		sizeX = z;
		char[][][] grid;
		ArrayList<int[]> vectors = makeVectors();
		final Random rng = new Random();

		for(int _counter = 0; _counter < 1000; _counter++) {
			grid = new char[sizeZ][sizeY][sizeX];
			for (int i = 0; i < words.length; i++) {
				for (int k = 0; k < 1000; k++) {
					int randomX = rng.nextInt(sizeX);
					int randomY = rng.nextInt(sizeY);
					int randomZ = rng.nextInt(sizeZ);

					final int randomPosition = rng.nextInt(26);
					int[] randomVector = vectors.get(randomPosition);
					if (!valid(randomVector, grid, words[i], randomZ, randomY, randomX)) continue;
					boolean possible = true;
					for (int t = 0; t < words[i].length(); t++) {
						char ch = grid[randomZ + t * randomVector[0]][randomY + t * randomVector[1]][randomX + t * randomVector[2]];
						if(Character.isLetter(ch) && ch != words[i].charAt(t)) {
							possible = false;
							break;
						}
					}
					if (possible) {
						insert(grid, randomVector, randomX, randomY, randomZ, words[i]);
						if (i == words.length - 1) {
							for (int i1 = 0; i1 < sizeZ; i1++) {
								for (int j = 0; j < sizeY; j++) {
									for (int k1 = 0; k1 < sizeX; k1++) {
										if((!Character.isLetter(grid[i1][j][k1])))
											grid[i1][j][k1] =  (char) (rng.nextInt(26) + 'a');
									}
								}
							}
							return grid;
						} else break;
					}
				}
			}

		}

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
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] != null) {
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
		/*final String[] words = new String[] { "apple", "orange", "pear", "peach", "durian", "lemon", "lime", "jackfruit", "plum", "grape", "apricot", "blueberry", "tangerine", "coconut", "mango", "lychee", "guava", "strawberry", "kiwi", "kumquat", "persimmon", "papaya", "longan", "eggplant", "cucumber", "tomato", "zucchini", "olive", "pea", "pumpkin", "cherry", "date", "nectarine", "breadfruit", "sapodilla", "rowan", "quince", "toyon", "sorb", "medlar" };
		final int xSize = 10, ySize = 10, zSize = 10;
		final char[][][] grid = wordSearch.make(words, xSize, ySize, zSize);
		exportGrid(grid, "grid.txt");

		final int[][][] locations = wordSearch.searchForAll(grid, words);
		exportLocations(locations, "locations.txt");*/




		char[][][] test = {
				{
						{'a', 'a', 'a', 'a'},
						{'o', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'}
				},
				{
						{'a', 'a', 'a', 'a'},
						{'o', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'}
				},
				{
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'a', 'a'},
						{'a', 'a', 'x', 'a'}
				}
		};

		final char[][][] grid = new char[][][] {
				{
					{'m', 'n', 'o', 'p'},
						{'p', 'r', 's', 't'},
						{'u', 'v', 'w', 'x'},
				}
		};


		char[][][] test3 = wordSearch.make(new String[] {"apple", "orange"}, 6, 7, 3);
		/*for(char[][] g : test3) {
			for (char[] t : g)
				System.out.println(Arrays.deepToString(g));
		}*/
		System.out.println(test3.length);
		System.out.println(test3[0].length);
		System.out.println(test3[0][0].length);
	}
}
