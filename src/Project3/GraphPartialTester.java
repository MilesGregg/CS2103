import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
	private static IMDBGraph imdbGraph;
	private static IMDBGraph testGraph;
	private static GraphSearchEngine searchEngine;

	public GraphPartialTester() throws IOException {

	}

	/**
	 * Verifies that there is no shortest path between a specific and actor and actress.
	 */
	@Test(timeout=5000)
	public void findShortestPath () throws IOException {
		final Node actor1 = testGraph.getActor("Actor1");
		final Node actress2 = testGraph.getActor("Actress2");
		System.out.println(actor1);
		System.out.println(actress2);
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
		assertNull(shortestPath);  // there is no path between these people
		final Node actress1 = testGraph.getActor("Actress1");
		final Node movie1 = testGraph.getMovie("Movie1 (2001)");
		List<Node> path1 = new ArrayList<Node>();
		path1.add(actor1);
		path1.add(movie1);
		path1.add(actress1);
		assertEquals(path1, searchEngine.findShortestPath(actor1, actress1));
	}

	@Test
	public void findShortestPath2() {
		final Node kev = imdbGraph.getActor("Bacon, Kevin (I)");
		final Node merr = imdbGraph.getActor("Merriam, Lucy");
		List<Node> path = new GraphSearchEngineImpl().findShortestPath(kev, merr);
		List<Node> expectedGraph = new ArrayList<>();
		expectedGraph.add(imdbGraph.getActor("Bacon, Kevin (I)"));
		expectedGraph.add(imdbGraph.getMovie("My One and Only (2009)"));
		expectedGraph.add(imdbGraph.getActor("Kneeream, David"));
		expectedGraph.add(imdbGraph.getMovie("Marley & Me (2008)"));
		expectedGraph.add(imdbGraph.getActor("Merriam, Lucy"));
		assertEquals(expectedGraph.toArray().length, path.toArray().length);
		for(int i = 0; i < path.size(); i++) {
			System.out.println(path.get(i));
			System.out.println(path.get(i).getName());
			System.out.println(expectedGraph.get(i));
			System.out.println(expectedGraph.get(i).getName());
			assertEquals(path.get(i), expectedGraph.get(i));
		}
		assertArrayEquals(expectedGraph.toArray(), path.toArray());
	}

	@BeforeClass
	/**
	 * Instantiates the graph
	 */
	public static void setUp() throws IOException {
		imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\IMDB\\actors.list","G:\\Darshan\\Downloads\\IMDB\\actresses.list");
		testGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list", "G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
		searchEngine = new GraphSearchEngineImpl();
	}

	@Test
	/**
	 * Just verifies that the graphs could be instantiated without crashing.
	 */
	public void finishedLoading () {
		assertTrue(true);
		// Yay! We didn't crash
	}

	@Test
	/**
	 * Verifies that a specific movie has been parsed.
	 */
	public void testSpecificMovie () {
		testFindNode(testGraph.getMovies(), "Movie1 (2001)");
	}

	@Test
	/**
	 * Verifies that a specific actress has been parsed.
	 */
	public void testSpecificActress () {
		testFindNode(testGraph.getActors(), "Actress2");
	}

	/**
	 * Verifies that the specific graph contains a node with the specified name
	 * @param nodes the IMDBGraph to search for the node
	 * @param name the name of the Node
	 */
	private static void testFindNode (Collection<? extends Node> nodes, String name) {
		boolean found = false;
		for (Node node : nodes) {
			if (node.getName().trim().equals(name)) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
