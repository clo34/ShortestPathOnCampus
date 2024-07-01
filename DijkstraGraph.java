// === CS400 File Header Information ===
// Name: Charles Lo
// Email: clo34@wisc.edu
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Gary Dahl
// Notes to Grader: <3

import java.util.*;
// for junit5 tests
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashtableMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // implement in step 5.3

        // initialize a ppq to store each node, a map to keep track of visited nodes and their costs
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        HashtableMap<NodeType, Double> visited = new HashtableMap<>();

        // starting node = 0
        pq.offer(new SearchNode(nodes.get(start), 0, null));

        while(!pq.isEmpty()){
            // extract node w/ min cost from pq
            SearchNode current = pq.poll();

            // return search node if end is reached
            if(current.node.data.equals(end)){
                return current;
            }

            //mark current node as visited w/ min cost
            visited.put(current.node.data, current.cost);

            // iterate edges
            for (Edge edge : current.node.edgesLeaving){
                // get the cost of path
                double newPathCost = current.cost + edge.data.doubleValue();
                // check if successor node is visited
                if(!visited.containsKey(edge.successor.data) || newPathCost < visited.get(edge.successor.data)){
                    pq.offer(new SearchNode(edge.successor, newPathCost, current));
                }
            }
        }
        // If no path from start to end was found, throw NoSuchElementException
        throw new NoSuchElementException("No path from " + start.toString() + " to " + end.toString());

    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // implement in step 5.4
        // use method above to compute shortest path
        SearchNode endNode = computeShortestPath(start, end);

        // if path is null there ain't no path
        if(endNode == null){
            throw new NoSuchElementException("No path from " + start.toString() + " to " + end.toString());
        }

        // trace back to collect the path data
        List<NodeType> pathData = new ArrayList<>();
        SearchNode current = endNode;
        while (current != null) {
            // add data of the current node to pathData list
            pathData.add(current.node.data);
            // move to the predecessor node
            current = current.predecessor;
        }

        // reverse list to get the correct order of nodes in path
        Collections.reverse(pathData);
        return pathData;
	}

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // implement in step 5.4
        // kinda the same start as above method
        SearchNode endNode = computeShortestPath(start, end);
        if(endNode == null){
            throw new NoSuchElementException("No path from " + start.toString() + " to " + end.toString());
        }
        // return the cost of sp
        return endNode.cost;
    }




    // TODO: implement 3+ tests in step 4.1
    /**
     * Helper method that sets up a DijkstraGraph instance before each test.
     */
    private DijkstraGraph<String, Double> createSampleGraph() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        // Insert nodes
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        // Insert edges
        graph.insertEdge("A", "B", 2.0);
        graph.insertEdge("A", "C", 3.0);
        graph.insertEdge("B", "D", 5.0);
        graph.insertEdge("C", "D", 2.0);
        return graph;
    }

    /**
     * Tests the shortestPathData method using a known example.
     */
    @Test
    public void testShortestPathData() {

        DijkstraGraph<String, Double> graph = createSampleGraph();

        // test shortest path from A to D
        List<String> shortestPath = graph.shortestPathData("A", "D");
        assertEquals(3, shortestPath.size()); // Expected path length
        assertEquals("A", shortestPath.get(0)); // Expected start node
        assertEquals("C", shortestPath.get(1)); // Expected intermediate node
        assertEquals("D", shortestPath.get(2)); // Expected end node
    }

    /**
     * Tests the shortestPathCost method using a known example.
     */
    @Test
    public void testShortestPathCost() {
        DijkstraGraph<String, Double> graph = createSampleGraph();
        double cost = graph.shortestPathCost("A", "D");
        assertEquals(5.0, cost); // Expected cost of the shortest path
    }

    /**
     * Tests the behavior when there is no path between existing nodes.
     */
    @Test
    public void testNoPath() {
        DijkstraGraph<String, Double> graph = createSampleGraph();
        graph.removeEdge("A", "B");
        graph.removeEdge("C", "D");

        // test shortest path data when no path exists
        try {
            List<String> shortestPath = graph.shortestPathData("A", "B");
            fail("Expected NoSuchElementException was not thrown");
        } catch (NoSuchElementException e) {
            assertEquals("No path from A to B", e.getMessage());
        }

        try {
            List<String> shortestPath = graph.shortestPathData("C", "D");
            fail("Expected NoSuchElementException was not thrown");
        } catch (NoSuchElementException e) {
            assertEquals("No path from C to D", e.getMessage());
        }

    }

}
