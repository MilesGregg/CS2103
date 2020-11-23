import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    /**
     * find the shortest path between two nodes
     * @param s the start node.
     * @param t the target node.
     * @return a list of nodes with the shortest path between s and t
     */
    @Override
    public List<Node> findShortestPath(Node s, Node t) {
        final Queue<Node> queue = new LinkedList<>();
        final Map<Node, Node> nodes = new HashMap<>();
        // put the s node in the queue
        queue.offer(s);
        while (queue.size() != 0) {
            // remove the first node in the queue
            final Node currentNode = queue.poll();
            // check for every node around the current one
            for (Node neighbor : currentNode.getNeighbors()) {
                // if the node isn't in the hash map then add it
                if (!nodes.containsKey(neighbor)) {
                    queue.offer(neighbor);
                    nodes.put(neighbor, currentNode);
                    if (t.equals(neighbor)) break; // we found t so break out
                }
            }
        }
        // if there isn't a key in the hashmap then no need to backtrack so just return null
        if (!nodes.containsKey(t)) return null;
        final List<Node> nodePath = new ArrayList<>();
        // add first node to the path which is t
        nodePath.add(t);
        // iterate through the whole path until we see s node
        while (!nodePath.get(nodePath.size()-1).equals(s)) {
            // add node to the path
            nodePath.add(nodes.get(nodePath.get(nodePath.size()-1)));
        }
        // reverse the node path since we backtracked
        return reverseNodeList(nodePath);
    }

    /**
     * Reverses the list of nodes
     * @param nodes - nodes to be reversed
     * @return the reversed list of nodes
     */
    private List<Node> reverseNodeList(List<Node> nodes) {
        final List<Node> output = new ArrayList<>();
        // start from the end and go to the beginning
        for (int i = nodes.size()-1; i >= 0; i--) {
            output.add(nodes.get(i));
        }
        // return the outputted reversed list
        return output;
    }
}
