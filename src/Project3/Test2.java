import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class Test2 {
    IMDBGraph imdbGraph;
    GraphSearchEngine searchEngine;

    /**
     * Verifies that there is a path between two nodes
     */
    @Test(timeout=10000)
    public void findValidPath() {
        final Node actor1 = imdbGraph.getActor("King, Brooke");
        final Node actor2 = imdbGraph.getActor("King, C. Stephanie");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
        assertNotNull(shortestPath);  // there is a path between these people
    }

    /**
     * Verifies that there is no shortest path between a specific and actor and actress.
     */
    @Test(timeout=5000)
    public void findShortestPath () throws IOException {
        IMDBGraph imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list","G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
        GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
        final Node actor1 = imdbGraph.getActor("Actor1");
        final Node actress2 = imdbGraph.getActor("Actress2");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
        assertNull(shortestPath);  // there is no path between these people
    }

    /**
     * Verifies that there is a path between two nodes and that the path is correct
     */
    @Test(timeout=10000)
    public void findCorrectPath() {
        final Node actor1 = imdbGraph.getActor("King, Brooke");
        final Node actor2 = imdbGraph.getActor("King, C. Stephanie");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
        assertEquals(shortestPath.get(0).getName(), "King, Brooke");
        assertEquals(shortestPath.get(2).getName(), "Moore, Julianne");
        assertEquals(shortestPath.get(8).getName(), "King, C. Stephanie");
    }

    /**
     * Verifies that there is a path between two nodes and that the path length is correct
     */
    @Test(timeout=10000)
    public void findCorrectPathLength() {
        final Node actor1 = imdbGraph.getActor("McGuire, Dede");
        final Node actor2 = imdbGraph.getActor("King, Caitlyn");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
        assertEquals(shortestPath.size(), 11);
    }

    @Before
    /**
     * Instantiates the graph
     */
    public void setUp () throws IOException {

        imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\actors_toy.list","G:\\Darshan\\Downloads\\actresses_toy.list");

        searchEngine = new GraphSearchEngineImpl();

    }

    @Test
    /**
     * Ensures that an exception is thrown if the path is incorrect
     */
    public void failedSetup() throws IOException{
        try {
            imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list","G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
        } catch (Exception e){
            assertTrue(true);
        }
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
    public void testSpecificMovie () throws IOException {
        IMDBGraph imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list","G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
        GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
        testFindNode(imdbGraph.getMovies(), "Movie1 (2001)");
    }

    @Test
    /**
     * Verifies that a specific actress has been parsed.
     */
    public void testSpecificActress () throws IOException {
        IMDBGraph imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list","G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
        GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
        testFindNode(imdbGraph.getActors(), "Actress2");
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