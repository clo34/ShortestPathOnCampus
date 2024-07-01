import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Backend implements BackendInterface{
    GraphADT<String, Double> graph;
    List<String> locations = new ArrayList<String>();

    /**
     * Constructor
     * @param graph object to sture the backend's graph data
     */
    public Backend(GraphADT<String, Double> graph) {

        this.graph = graph;
        this.locations = new ArrayList<>();
     }

    @Override
    /**
     * Loads graph data from a dot file.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was a problem reading in the specified file
     */
    public void loadGraphData(String filename) throws IOException {
        try {
            Scanner fileScanner = new Scanner(new File(filename));

            // Skip the first line since it contains the graph declaration
            fileScanner.nextLine();

            while (fileScanner.hasNextLine()) {
                String newLine = fileScanner.nextLine().trim();
                //System.out.println( "New Line = " + newLine); //print it

                if (newLine.endsWith(";")) {
                    // Remove the trailing semicolon
                    newLine = newLine.substring(0, newLine.length() - 1);

                    // Split the line by "->"
                    String[] parts = newLine.split("->");

                    // Extract source and destination nodes
                    String source = parts[0].trim().replaceAll("\"", "");
                    String destination = parts[1].split("\\[")[0].trim().replaceAll("\"", "");

                    // Extract travel time (seconds)
                    double weight = Double.parseDouble(parts[1].split("=")[1].replaceAll("[^0-9.]", ""));


                    //Debug parsing
//                    System.out.println("Source = " + source);
//                    System.out.println("Destination = " +destination);
//                    System.out.println("Weight = " + weight);


                    // Add nodes and edge to the graph
                    if (!locations.contains(source)) {
                        locations.add(source);
                    }
                    if (!locations.contains(destination)) {
                        locations.add(destination);
                    }
                    graph.insertNode(source);
                    graph.insertNode(destination);
                    graph.insertEdge(source, destination, weight);

                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new IOException("Trouble reading in specified file: " + e.getMessage());
        }
    }

    @Override
    /**
     * Returns a list of all locations (nodes) available on the backend's graph.
     * @return list of all location names
     */
    public List<String> getListOfAllLocations() {
        return locations;
            
    }

    @Override
    /**
     * Returns the sequence of locations along the shortest path from startLocation to endLocation, or
     * en empty list if no such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the nodes along the shortest path from startLocation to endLocation, or
     *         an empty list if no such path exists
     */
    public List<String> findShortestPath(String startLocation, String endLocation) {
        return graph.shortestPathData(startLocation, endLocation);
    }

    @Override
    /**
     * Returns the walking times in seconds between each two nodes on the shortest path from startLocation
     * to endLocation, or an empty list of no such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the walking times in seconds between two nodes along the shortest path from
     *         startLocation to endLocation, or an empty list if no such path exists
     */
    public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
        List<String> path = findShortestPath(startLocation, endLocation);
        List<Double> intermediateCost = new ArrayList<Double>();

        for (int i = 0; i < path.size() - 1; i++) {
            intermediateCost.add(graph.getEdge(path.get(i), path.get(i + 1)));
        }
        return intermediateCost;
    }

    @Override
    /**
     * Returns the sequence of locations along the shortest path from startLocation to endLocation including
     * the third location viaLocation, or an empty list if no such path exists.
     * en empty list if no such path exists.
     * @param startLocation the start location of the path
     * @param viaLocation a location that the path show lead through
     * @param endLocation the end location of the path
     * @return a list with the nodes along the shortest path from startLocation to endLocation including
     *         viaLocation, or an empty list if no such path exists
     */
    public List<String> findShortestPathVia(String startLocation, String viaLocation, String endLocation) {
        
        List<String> tempList = graph.shortestPathData(startLocation, viaLocation); 
        // make a deep copy for path one
        List<String> pathOne = new ArrayList<String>();
        for (int i = 0; i < tempList.size(); i++) {
            pathOne.add(tempList.get(i));
        }
        pathOne.remove(pathOne.size() - 1); // remove for redundant viaLocation

        try {
            List<String> pathTwo = new ArrayList<String>();
            pathTwo = graph.shortestPathData(viaLocation, endLocation);

            pathOne.addAll(pathTwo);
            return pathOne; // return path via if successfully added
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<String>(); // return empty list if path via fails to add
        }
    }

    @Override
    /**
     * Returns the walking times in seconds between each two nodes on the shortest path from startLocation
     * to endLocation through viaLocation, or an empty list of no such path exists.
     * @param startLocation the start location of the path
     * @param viaLocation a location that the path show lead through
     * @param endLocation the end location of the path
     * @return a list with the walking times in seconds between two nodes along the shortest path from
     *         startLocation to endLocationthrough viaLocation, or an empty list if no such path exists
     */
    public List<Double> getTravelTimesOnPathVia(String startLocation, String viaLocation, String endLocation) {

        List<String> path = findShortestPathVia(startLocation, viaLocation, endLocation);
        List<Double> intermediateCost = new ArrayList<Double>();

        if (!path.isEmpty()) {
            for (int i = 0; i < path.size() - 1; i++) {
                intermediateCost.add(graph.getEdge(path.get(i), path.get(i + 1)));
            }
        }
        return intermediateCost;
    }
    
}
