import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Actor implements Node{
    List<Node> movies = new ArrayList<>();
    String name;

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends Node> getNeighbors() {
        return movies;
    }

    public void addNeighbor(Node movie){
        movies.add(movie);
    }
}
