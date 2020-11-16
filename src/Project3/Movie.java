import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Movie implements Node{
    List<Actor> actors = new ArrayList<>();
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
        return actors;
    }

    public void addNeighbor(Actor actor){
        actors.add(actor);
    }
}
