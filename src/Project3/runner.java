import java.io.IOException;

public class runner {
    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        IMDBGraphImpl i = new IMDBGraphImpl("G:\\Darshan\\Downloads\\IMDB\\actors.list","G:\\Darshan\\Downloads\\IMDB\\actresses.list");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        Node n = i.getActor("Bacon, Kevin (I)");
        System.out.println("KEVIN BACON");
        System.out.println(n.getNeighbors().size());
        for(Node q : n.getNeighbors())
            System.out.println(q.getName());


        for(Node j : i.getMovie("Sleeping with the Enemy 2 (2004)").getNeighbors())
            System.out.println(j.getName());
        System.out.println("NUMERO UNO");
        for(Node j : i.getMovie("Sleeping with the Enemy (1991)").getNeighbors())
            System.out.println(j.getName());
        System.out.println("JULIA");
        for(Node j : i.getActor("Roberts, Julia (I)").getNeighbors())
            System.out.println(j.getName());
    }
}
