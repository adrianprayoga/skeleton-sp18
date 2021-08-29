import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    Map<Long, Node> nodes = new HashMap<>();
    Map<Long, Node> locationNodes = new HashMap<>();
    Map<Long, Edge> edges = new HashMap<>();
    Trie locationNames = new Trie();

    static class Node {
        List<Node> neighbors = new ArrayList<>(); // Long (id of node)
        HashMap<Long, List<Long>> edgeMapping = new HashMap<>();
        Long id;
        Double lon;
        Double lat;
        boolean connected;
        String name;
        boolean isLocation;

        public Node(Long id, Double lon, Double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
        }

        public void addNeighbor(Long edgeId, Node neighborNode) {
            this.neighbors.add(neighborNode);

            List<Long> edgeIds = this.edgeMapping.get(neighborNode.id);
            if (edgeIds == null) {
                edgeIds = new ArrayList<>();
            }
            edgeIds.add(edgeId);
            this.edgeMapping.put(neighborNode.id, edgeIds);
        }

        public void markAsConnected() {
            this.connected = true;
        }

        public void setName(String name) {
            this.name = name;
            this.isLocation = true;
        }
    }

    static class Edge {
        public Long id;
        public String name = "";

        public Edge(Long id) {
            this.id = id;
        }

        public void setName (String name) {
            this.name = name;
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    void addNode (Long id, Node node) {
        this.nodes.put(id, node);
    }

    void addEdges (Long id, Edge edge) {
        this.edges.put(id, edge);
    }

    void addLocation (Long id, String name, Node node) {
        this.locationNodes.put(id, node);
        this.locationNames.addString(name, id);
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Long> idsToRemove = new HashSet<>();
        for (Long id : nodes.keySet()) {
            if (!nodes.get(id).connected) {
                idsToRemove.add(id);
            }
        }

        for (Long id : idsToRemove) {
            nodes.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Node node = nodes.get(v);
        if (node == null) {
            return null;
        }

        return node.neighbors.stream().map(n -> n.id).collect(Collectors.toList());
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        // Can we do better than O(N) ?
        Double closesDistance = Double.MAX_VALUE;
        Long closestIndex = null;
        for (Map.Entry<Long, Node> entry : nodes.entrySet()) {
            Long id = entry.getKey();
            Node node = entry.getValue();

            Double distance = distance(node.lon, node.lat, lon, lat);
            if (distance < closesDistance) {
                closesDistance = distance;
                closestIndex = id;
            }
        }

        return closestIndex;
    }

    Node getNode(Long id) {
        // assume id exists
        return nodes.get(id);
    }

    Edge getEdge(Long id) {
        // assume id exists
        return edges.get(id);
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        Node node = nodes.get(v);
        if (node == null) {
            return 0;
        }
        return node.lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        Node node = nodes.get(v);
        if (node == null) {
            return 0;
        }
        return node.lat;
    }

    List<String> getLocationsByPrefix(String prefix) {
        List<Long> ids = locationNames.getWordsWithPrefix(prefix);
        return ids.stream().map(locationId -> locationNodes.get(locationId))
                .filter(Objects::nonNull)
                .map(node -> node.name)
                .collect(Collectors.toList());
    }

    List<Map<String, Object>> getLocations(String locationName) {
        List<Long> ids = locationNames.getWordsWithExactMatch(locationName.toLowerCase(Locale.ROOT));
        return ids.stream().map(locationId -> locationNodes.get(locationId))
                .filter(Objects::nonNull)
                .map(node -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("lat", node.lat);
                    map.put("lon", node.lon);
                    map.put("name", node.name);
                    map.put("id", node.id);

                    return map;
                })
                .collect(Collectors.toList());
    }

}
