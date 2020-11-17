import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {
    @Override
    public List<Node> findShortestPath(Node s, Node t) {
        List<Node> path = null;
        List<Node> visitedNodes = new ArrayList<>();
        Queue<Node> nodesToVisit = new LinkedList<>();
        Map<Node, Integer> distance = new HashMap<>();

        nodesToVisit.add(s);
        distance.put(s, 0);
        while (nodesToVisit.size() > 0) {
            Node node = nodesToVisit.poll();
            visitedNodes.add(node);
            path = new ArrayList<>();
            if (node.equals(t)) {
                for (int i = 0; i < distance.get(node); i++) {
                    path.add(node);
                    for (Node neighbor : node.getNeighbors()) {
                        if (visitedNodes.contains(neighbor) && distance.get(neighbor) == distance.get(node)-1) {
                            node = neighbor;
                            break;
                        }
                    }
                }
                path.add(s);
                break;
            } else {
                for (Node k : node.getNeighbors()) {
                    if (!visitedNodes.contains(k)) {
                        nodesToVisit.add(k);
                        distance.put(k, distance.get(node) + 1);
                    }
                }
            }
        }

        if (path != null) Collections.reverse(path);
        return path;
    }
}
