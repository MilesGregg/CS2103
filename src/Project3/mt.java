import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class mt {
    public static void main(String[] args) throws IOException {
        IMDBGraphImpl imdbGraph = new IMDBGraphImpl("G:\\Darshan\\Downloads\\IMDB\\actors.list","G:\\Darshan\\Downloads\\IMDB\\actresses.list");
        List<Node> expectedGraph = new ArrayList<>();
        expectedGraph.add(imdbGraph.getActor("Bacon, Kevin (I)"));
        expectedGraph.add(imdbGraph.getMovie("My One and Only (2009)"));
        expectedGraph.add(imdbGraph.getActor("Kneeream, David"));
        expectedGraph.add(imdbGraph.getMovie("Marley & Me (2008)"));
        expectedGraph.add(imdbGraph.getActor("Merriam, Lucy"));
        for(int i = 0; i < expectedGraph.size(); i++)
            System.out.println(expectedGraph.get(i).getName());
    }
    @Test
    public void findShortestPath2() throws IOException {

    }
}
