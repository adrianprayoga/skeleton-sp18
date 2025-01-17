import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *
 *  You may find the CSCourseGraphDB and CSCourseGraphDBHandler examples useful.
 *
 *  The idea here is that some external library is going to walk through the XML
 *  file, and your override method tells Java what to do every time it gets to the next
 *  element in the file. This is a very common but strange-when-you-first-see it pattern.
 *  It is similar to the Visitor pattern we discussed for graphs.
 *
 *  @author Alan Yao, Maurice Lee
 */
public class GraphBuildingHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private boolean isValidRoad = false;
    private GraphDB.Edge currentEdge;
    private GraphDB.Node currentNode;
    private Deque<GraphDB.Node> connectedNodes = new ArrayDeque<>();
    private final GraphDB g;

    /**
     * Create a new GraphBuildingHandler.
     * @param g The graph to populate with the XML data.
     */
    public GraphBuildingHandler(GraphDB g) {
        this.g = g;
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        /* Some example code on how you might begin to parse XML files. */
        if (qName.equals("node")) {
            /* We encountered a new <node...> tag. */
            activeState = "node";
            Long id = Long.valueOf(attributes.getValue("id"));
            Double lon = Double.valueOf(attributes.getValue("lon"));
            Double lat = Double.valueOf(attributes.getValue("lat"));
//            System.out.println("Node id: " + id);
//            System.out.println("Node lon: " + lon);
//            System.out.println("Node lat: " + lat);

            this.currentNode = new GraphDB.Node(id, lon, lat);
            g.addNode(id, currentNode);

        } else if (qName.equals("way")) {
            /* We encountered a new <way...> tag. */
            activeState = "way";
            Long id = Long.valueOf(attributes.getValue("id"));
            currentEdge = new GraphDB.Edge(id);
//            System.out.println("Beginning a way...");
        } else if (activeState.equals("way") && qName.equals("nd")) {
            /* While looking at a way, we found a <nd...> tag. */
//            System.out.println("Id of a node in this way: " + attributes.getValue("ref"));
            Long nodeId = Long.valueOf(attributes.getValue("ref"));
            GraphDB.Node n = g.nodes.get(nodeId);
            if (n != null) {
                connectedNodes.addLast(n);
            }

        } else if (activeState.equals("way") && qName.equals("tag")) {
            /* While looking at a way, we found a <tag...> tag. */
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            if (k.equals("maxspeed")) {
                //System.out.println("Max Speed: " + v);
                //Not required
                /* TODO set the max speed of the "current way" here. */
            } else if (k.equals("highway")) {
                if (ALLOWED_HIGHWAY_TYPES.contains(v)) {
                    isValidRoad = true;
                }
            } else if (k.equals("name")) {
                currentEdge.setName(v);
            }
//            System.out.println("Tag with k=" + k + ", v=" + v + ".");
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                .equals("name")) {
            currentNode.setName(attributes.getValue("v"));
            g.addLocation(currentNode.id, attributes.getValue("v").toLowerCase(Locale.ROOT), currentNode);
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            if (isValidRoad && connectedNodes.size() > 1) {
                g.addEdges(currentEdge.id, currentEdge);

                GraphDB.Node n1 = connectedNodes.pollFirst();
                n1.markAsConnected();
                GraphDB.Node n2 = connectedNodes.pollFirst();
                while(n2 != null) {
                    n2.markAsConnected();

                    n1.addNeighbor(currentEdge.id, n2);
                    n2.addNeighbor(currentEdge.id, n1);
                    n1 = n2;
                    n2 = connectedNodes.pollFirst();
                }
            }

            activeState = "";
            isValidRoad = false;
            connectedNodes = new ArrayDeque<>();
            currentEdge = null;

            /* We are done looking at a way. (We finished looking at the nodes, speeds, etc...)*/
            /* Hint1: If you have stored the possible connections for this way, here's your
            chance to actually connect the nodes together if the way is valid. */
//            System.out.println("Finishing a way...");
        } else if (qName.equals("node")) {
            currentNode = null;
        }
    }

}
