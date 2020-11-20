import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    @Override
    public List<Node> findShortestPath(Node s, Node t) {
        final Queue<Node> queue = new LinkedList<>();
        final Map<Node, Node> nodes = new HashMap<>();
        queue.offer(s);
        while (queue.size() != 0) {
            final Node currentNode = queue.poll();
            for (Node neighbor : currentNode.getNeighbors()) {
                if (!nodes.containsKey(neighbor)) {
                    queue.offer(neighbor);
                    nodes.put(neighbor, currentNode);
                    if (t.equals(neighbor)) break; // found!
                }
            }
        }
        if (!nodes.containsKey(t)) return null;
        final List<Node> nodePath = new ArrayList<>();
        nodePath.add(t);
        while (!nodePath.get(nodePath.size()-1).equals(s)) {
            nodePath.add(nodes.get(nodePath.get(nodePath.size()-1)));
        }

        Collections.reverse(nodePath);
        return nodePath;
    }
}
