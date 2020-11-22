import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.io.*;

/**
 * Code to test Project 3
 */
public class GabrielTest {
    private IMDBGraph imdbGraph, testGraph;
    private GraphSearchEngine searchEngine;

    /**
     * Verifies that there is no shortest path between a specific and actor and actress.
     */
    @Test(timeout = 5000)
    public void findShortestPath() {
        final Node actor1 = testGraph.getActor("Actor1");
        final Node actress2 = testGraph.getActor("Actress2");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
        assertNull(shortestPath);  // there is no path between these people
    }

    /**
     * Verifies that there is a shortest path between a specific and actor and actress.
     */
    @Test(timeout = 5000)
    public void findShortestPathNotNull() {
        final Node actor1 = testGraph.getActor("Actor1");
        final Node movie1 = testGraph.getMovie("Movie1 (2001)");
        final Node actress1 = testGraph.getActor("Actress1");
        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress1);
        printOutNodes(shortestPath);
        assertEquals(new ArrayList<>(Arrays.asList(actor1, movie1, actress1)), shortestPath);
    }


    /**
     * Verifies that there is a shortest path between a specific and actor and actress with two hops.
     */
    @Test(timeout = 5000)
    public void findShortestPathTwoHop() {
        final Node actress2 = testGraph.getActor("Actress2");
        final Node movie2 = testGraph.getMovie("Movie2 (2002)");
        final Node actor2 = testGraph.getActor("Actor2");
        final Node movie4 = testGraph.getMovie("Movie4 (2004)");
        final Node actor4 = testGraph.getActor("Actor4");
        final List<Node> shortestPath = searchEngine.findShortestPath(actress2, actor4);
        printOutNodes(shortestPath);
        assertEquals(new ArrayList<>(Arrays.asList(actress2, movie2, actor2, movie4, actor4)), shortestPath);
    }

    /**
     * Verifies that there is a shortest path between a specific and actor and actress with two hops.
     */
    @Test(timeout = 5000)
    public void findShortestPathUsingIMDB() {
        final Node Kevin = imdbGraph.getActor("Darnell, Linda");
        final Node movie2 = imdbGraph.getMovie("Black Spurs (1965)");
        final Node actor2 = imdbGraph.getActor("Baird, Jeanne");
        final Node movie4 = imdbGraph.getMovie("Get Outta Town (1960)");
        final Node Brad = imdbGraph.getActor("Casey, Val");
        final List<Node> shortestPath = searchEngine.findShortestPath(Kevin, Brad);
        System.out.println("printing out nodes:");
        printOutNodes(shortestPath);
//        assertEquals(new ArrayList<>(Arrays.asList(actress2, movie2, actor2, movie4, actor4)), shortestPath);
    }

    /**
     * Instantiates the graph
     */
    @Before
    public void setUp() throws IOException {
        imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\IMDB\\actors.list","G:\\Darshan\\Downloads\\IMDB\\actresses.list");
        testGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actors_test.list", "G:\\Darshan\\Downloads\\CS2103\\src\\Project3\\actresses_test.list");
        searchEngine = new GraphSearchEngineImpl();
    }

    /**
     * Just verifies that the graphs could be instantiated without crashing.
     */
    @Test
    public void finishedLoading() {
        assertTrue(true);
        // Yay! We didn't crash
    }



    /**
     * Verifies that specific movies have been parsed.
     */
    @Test
    public void testSpecificMovies() {
        testFindNode(imdbGraph.getMovies(), "Caceria de judiciales (1997)");
        testFindNode(imdbGraph.getMovies(), "El secuestro de un periodista (1992)");
        testFindNode(imdbGraph.getMovies(), "Violencia en la sierra (1995)");
        assertTrue(imdbGraph.getMovie("Violencia en la sierra (1995)").getNeighbors()
                .contains(imdbGraph.getActor("'La Chispa', Tony")));
        testFindNode(imdbGraph.getMovies(), "A Woman's Face (1941)");
        testFindNode(imdbGraph.getMovies(), "Bikini Bistro (1995)");
        testFindNode(imdbGraph.getMovies(), "Erotik auf der Schulbank (1968)");
        assertTrue(imdbGraph.getMovie("Erotik auf der Schulbank (1968)").getNeighbors()
                .contains(imdbGraph.getActor("Aabel, Hauk (II)")));
    }

    /**
     * Verifies that specific performers have been parsed correctly
     */
    @Test
    public void testSpecificPerformers() {
        // Test for an actor
        testFindNode(imdbGraph.getActors(), "'La Chispa', Tony");
        final Node actor = imdbGraph.getActor("'La Chispa', Tony");
        assertTrue(actor.getNeighbors().contains(imdbGraph.getMovie("Caceria de judiciales (1997)")));
        assertTrue(actor.getNeighbors().contains(imdbGraph.getMovie("El secuestro de un periodista (1992)")));
        assertTrue(actor.getNeighbors().contains(imdbGraph.getMovie("Violencia en la sierra (1995)")));
        for(Node i : actor.getNeighbors())
            System.out.println(i.getName());
        assertEquals(3, actor.getNeighbors().size());
        // Test for an actress
        testFindNode(imdbGraph.getActors(), "Aabel, Hauk (II)");
        final Node actress = imdbGraph.getActor("Aabel, Hauk (II)");
        assertTrue(actress.getNeighbors().contains(imdbGraph.getMovie("A Woman's Face (1941)")));
        assertTrue(actress.getNeighbors().contains(imdbGraph.getMovie("Bikini Bistro (1995)")));
        assertTrue(actress.getNeighbors().contains(imdbGraph.getMovie("Erotik auf der Schulbank (1968)")));
        assertEquals(3, actress.getNeighbors().size());
    }

    /**
     * Verifies the size of the IMDBGraphImpl after reading 10k lines is within the provided tolerance
     */
    @Test
    public void testParsingTolerance() {
        final int size = imdbGraph.getActors().size();
        System.out.println("There were " + size + " performers read from the 10k line files.");
        assertTrue(size >= 2100 && size <= 2300);
    }

    /**
     * Verifies that the specific graph's nodes contains a node with the specified name
     *
     * @param nodes the nodes of the graph to search from
     * @param name  the name of the Node
     */
    private static void testFindNode(Collection<? extends Node> nodes, String name) {
        boolean found = false;
        for (Node node : nodes) {
            if (node.getName().trim().equals(name)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Prints out a list of nodes nicely
     *
     * @param nodes the nodes to print out
     */
    private void printOutNodes(List<Node> nodes) {
        if (nodes == null) {
            System.out.println("Nodes list was null");
            return;
        }
        StringBuilder nodesStr = new StringBuilder();
        int length = nodes.size();
        for (int i = 0; i < nodes.size(); ++i) {
            final String currNode = (nodes.get(i) == null) ? "null" : nodes.get(i).getName();
            if (i == length - 1) nodesStr.append(currNode);
            else nodesStr.append(currNode).append(" -> ");
        }
        System.out.println(nodesStr.toString());
    }

//    /**
//     * Verifies that there is no shortest path between a specific and actor and actress.
//     */
//    @Test(timeout = 5000)
//    public void findShortestPath() throws IOException {
//        imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
//        final Node actor1 = imdbGraph.getActor("Actor1");
//        final Node actress2 = imdbGraph.getActor("Actress2");
//        final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
//        assertNull(shortestPath);  // there is no path between these people
//    }
}
