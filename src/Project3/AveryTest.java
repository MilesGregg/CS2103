import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class AveryTest {
    IMDBGraph imdbGraph;
    GraphSearchEngine searchEngine;

    /**
     * Verifies that there is no shortest path between a specific and actor and actress.
     */
    @Test(timeout=5000)
    public void findShortestPath () throws IOException {
        imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list","G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
        final Node actor1 = imdbGraph.getActor("Actor1");
        final Node actress2 = imdbGraph.getActor("Actress2");
        final Node actress1 = imdbGraph.getActor("Actress1");
        final Node movie1 = imdbGraph.getMovie("Movie1 (2001)");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
        assertNull(shortestPath);  // there is no path between these people
        List<Node> path1 = new ArrayList<Node>();
        path1.add(actor1);
        path1.add(movie1);
        path1.add(actress1);
        assertEquals(path1, searchEngine.findShortestPath(actor1, actress1));
    }

    @Before
    /**
     * Instantiates the graph
     */
    public void setUp () throws IOException {
        imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list","G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
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
        testFindNode(imdbGraph.getMovies(), "Movie1 (2001)");
    }

    @Test
    /**
     * Verifies that a specific actress has been parsed.
     */
    public void testSpecificActress () {
        testFindNode(imdbGraph.getActors(), "Actress2");
    }

    /**
     * Verifies that the specific graph contains a node with the specified name
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

    @Test
    /**
     * test lists have been modified to include an additional Actor4 and Actor5
     * Actor4: Movie4 (2004) (TV) Movie5 (2005) Movie6 (2006)
     * Actor5: Movie4 (2004) (TV) Movie2 (2002) Movie6 (2002)
     * Additionaly Movie3 (2003) has been added to Actor2
     */
    public void expandedGraphSearch() {
        //Go from Actor4 to Actress2
        List<Node> path = new ArrayList<Node>();
        path.add(imdbGraph.getActor("Actor4"));
        path.add(imdbGraph.getMovie("Movie6 (2006)"));
        path.add(imdbGraph.getActor("Actor5"));
        path.add(imdbGraph.getMovie("Movie2 (2002)"));
        path.add(imdbGraph.getActor("Actress2"));
        assertEquals(path, searchEngine.findShortestPath(imdbGraph.getActor("Actor4"),imdbGraph.getActor("Actress2")));
    }

    /**
     * Test to see if can parse 10,000 lines without crashing or overflowing
     * @throws IOException
     */
    @Test
    public void parsesTenThousandLines() throws IOException {
        IMDBGraphImpl graphTest = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list","G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");

        assertTrue(true);

        //Located at Line 3045
        testFindNode(graphTest.getActors(), "A Heygum, Runa");

        //TV actor with quotation marks, located at line 2989
        assertNull(graphTest.getActor("A Camp"));

        //TV actor with (TV), located at line 2875
        assertNull(graphTest.getActor("883"));

        //Located at Line 1366
        testFindNode(graphTest.getMovies(), "Big Apple (2002)");

        //TV movie, located at line 931
        assertNull(graphTest.getMovie("2000 MTV Movie Awards (2000)"));
    }

    @Test
    /**
     * Test to see if can parse entire imdb list file.
     * @throws IOException
     */
    public void parsesFullIMDBList() throws IOException {
        IMDBGraphImpl graphTest = new IMDBGraphImpl("G:\\Darshan\\Downloads\\IMDB (1)\\actors.list","G:\\Darshan\\Downloads\\IMDB (1)\\actresses.list");

        assertTrue(true);
    }
}