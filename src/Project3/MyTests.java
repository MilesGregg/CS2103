import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class MyTests {

    public static void main(String[] args) throws IOException {
        IMDBGraph imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\IMDB\\actors.list","G:\\Darshan\\Downloads\\IMDB\\actresses.list");
        boolean success1 = true;
        for(Node a : imdbGraph.getActors()){
            if(a.getNeighbors().size() == 0){
                System.out.println("TEST 1 FAILED");
                success1 = false;
                break;
            }
            if(!a.getName().equals(imdbGraph.getActor(a.getName()).getName())) {
                System.out.println("TEST 1 FAILED");
                break;
            }
        }
        if(success1) System.out.println("TEST 1 PASSED");

        boolean success2 = true;
        final Node kev = imdbGraph.getActor("Bacon, Kevin (I)");
        final Node merr = imdbGraph.getActor("Merriam, Lucy");
        List<Node> path = new GraphSearchEngineImpl().findShortestPath(kev, merr);
        List<Node> expectedGraph = new ArrayList<>();
        expectedGraph.add(imdbGraph.getActor("Bacon, Kevin (I)"));
        expectedGraph.add(imdbGraph.getMovie("My One and Only (2009)"));
        expectedGraph.add(imdbGraph.getActor("Kneeream, David"));
        expectedGraph.add(imdbGraph.getMovie("Marley & Me (2008)"));
        expectedGraph.add(imdbGraph.getActor("Merriam, Lucy"));
        if(expectedGraph.toArray().length == path.toArray().length) System.out.println("TEST 2 PASSED");
        else System.out.println("TEST 2 FAILED");

        if(imdbGraph.getMovies().size() == 863664) System.out.println("TEST 3 PASSED");
        else System.out.println("TEST 3 FAILED");

        if(imdbGraph.getActors().size() == 2812637) System.out.println("TEST 4 PASSED");
        else System.out.println("TEST 4 FAILED");


        HashSet<Node> visited = new HashSet<>();
        boolean success5 = true;
        for(Node a : imdbGraph.getActors()){
            if(visited.contains(a)){
                System.out.println("TEST 5 FAILED");
                System.out.println(a.getName());
                System.out.println(a.getNeighbors());
                success5 = false;
                break;
            } else {
                visited.add(a);
            }
        }
        if(success5) System.out.println("TEST 5 PASSED");

        System.out.println(imdbGraph.getMovies().size());
        System.out.println(imdbGraph.getActors().size());



    }
    @Test
    public void findShortestPath2() {

    }
}
