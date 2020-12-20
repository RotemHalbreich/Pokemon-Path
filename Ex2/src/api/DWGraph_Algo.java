package api;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.parseDouble;

/**
 * This class represents a Directed Weighted Graph Theory Algorithms including:
 *
 * graph - represents the basic directed weighted graph
 * algorithm - represents graph algorithm using only in shortestPath & shortestPathDist
 *
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 *
 * @author Shaked Aviad & Rotem Halbreich
 */
public class DWGraph_Algo implements dw_graph_algorithms {
    private static final String NOT_VISITED = "white", VISITED = "green", FINISH = "black";
    private directed_weighted_graph graph;
    private DWGraph_DS algorithm;

    /**
     * Constructor:
     */
    public DWGraph_Algo() {
        graph = new DWGraph_DS();
    }

    /**
     * Initializes the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        graph = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return directed_weighted_graph
     */
    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /**
     * Computes a deep copy of this weighted graph.
     *
     * @return directed_weighted_graph - a clone of the graph
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS copy = new DWGraph_DS();
        for (node_data n : graph.getV())
            copy.addNode(new Node(n));

        for (node_data n : graph.getV()) {
            for (edge_data e : graph.getE(n.getKey())) {
                copy.connect(new Edge(e));
            }
        }
        return copy;
    }

    /**
     * Returns true iff there is a valid path from each vertex to each other vertex,
     * and from every other vertex there's a path to get back to the first vertex.
     *
     * @return boolean
     */
    @Override
    public boolean isConnected() {

        if (graph == null) return false;
        if (graph.getV().isEmpty() || graph.nodeSize() == 1) return true;

        DWGraph_DS forward = (DWGraph_DS) copy();
        if (!direction(forward)) return false;

        DWGraph_DS backwards = (DWGraph_DS) forward.reverse();
        if (!direction(backwards)) return false;

        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return double
     * @throws RuntimeException - if one or more vertices don't exist in the graph
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        createGraphAlgorithm();
        if (graph.getNode(src) == null || graph.getNode(dest) == null)
            throw new RuntimeException("Err:Invalid search src or dest not exists in base graph");
        if (src == dest) return 0;

        Node_Algo s = (Node_Algo) algorithm.getNode(src);
        s.setPrice(0);
        s.setPrev(null);
        return shortestPathDist(src, dest, new PriorityBlockingQueue<node_data>(algorithm.getV()));

    }

    /**
     * Returns the shortest path between src to dest vertices as an ordered List of nodes:
     * (src)--> (n1)-->(n2)-->...-->(dest)
     * If no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end node
     * @return List<node_data>
     * @throws  RuntimeException - if one or more vertices don't exist in the graph
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (shortestPathDist(src, dest) == -1) return null;
        return shortestPath(src, dest, new LinkedList<node_data>());
    }

    /**
     * Saves this weighted directed graph to the given
     * file name - in JSON format using GSON library
     *
     * @param file - the file name (may include a relative path).
     * @return boolean (true - iff the file was successfully saved)
     */
    @Override
    public boolean save(String file) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(graph);
            PrintWriter pw = new PrintWriter(file);
            pw.print(json);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method loads a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * Reads JSON format of the basic graph and JSON format of the data package.
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new DWGraph_DSJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            graph = gson.fromJson(reader, directed_weighted_graph.class);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reads the graph from Json
     *
     * @param s
     * @return DWGraph_DS
     */
    public directed_weighted_graph readFromJson(String s) {

        directed_weighted_graph algo = new DWGraph_DS();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(directed_weighted_graph.class, new DWGraph_DSJsonDeserializer());
        Gson gson = builder.create();
        algo = gson.fromJson(s, directed_weighted_graph.class);
        return algo;
    }

    /**
     * Returns this graph's algorithm as a String.
     *
     * @return String
     */
    public String toString() {
        return graph.toString();
    }

    /**
     * Using equals method of the basic graph.
     *
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj) {
        return graph.equals(obj);
    }

    ///////// Private Methods /////////

    /**
     * Help Function: Checks if there's a path between a vertex to any other vertex in the graph.
     *
     * @param g
     * @param n
     */
    private void isConnected(directed_weighted_graph g, node_data n) {
        LinkedList<node_data> list = new LinkedList<node_data>();
        n.setInfo(VISITED);
        list.addFirst(n);
        while (!list.isEmpty()) {
            node_data currNode = list.removeFirst();
            Iterator<edge_data> itr = g.getE(currNode.getKey()).iterator();
            while (itr.hasNext()) {
                node_data ni = g.getNode(itr.next().getDest());
                if (ni.getInfo().equals(NOT_VISITED)) {
                    ni.setInfo(VISITED);
                    list.addFirst(ni);
                }
            }
            currNode.setInfo(FINISH);
        }
    }

    /**
     * Help function: checks after isConnected if there's a valid path between all the vertices.
     *
     * @param g
     * @return boolean
     */
    private boolean direction(DWGraph_DS g) {
        for (node_data n : g.getV()) n.setInfo(NOT_VISITED);
        Node first = (Node) g.getV().iterator().next();
        isConnected(g, first);
        for (node_data n : g.getV()) {
            if (!n.getInfo().equals(FINISH))
                return false;
        }
        return true;
    }

    /**
     * Help function: calculate the shortest path from src to dest vertices using Dijkstra's algorithm
     * and updates the vertices as previous nodes using algorithm graph.
     *
     * @param src
     * @param dest
     * @param queue
     * @return double
     */
    private double shortestPathDist(int src, int dest, PriorityBlockingQueue<node_data> queue) {
        while (!queue.isEmpty()) {
            Node_Algo currNode = (Node_Algo) queue.remove();
            if (currNode.getKey() == dest || currNode.getPrice() == MAX_VALUE) {
                if (currNode.getPrice() == MAX_VALUE) return -1;
                return currNode.getPrice();
            }
            for (edge_data e : algorithm.getE(currNode.getKey())) {
                Node_Algo ni = (Node_Algo) algorithm.getNode(e.getDest());
                double weight = currNode.getPrice() + e.getWeight();
                if (ni.getInfo().equals(NOT_VISITED) && ni.getPrice() > weight) {
                    ni.setPrice(weight);
                    queue.remove(ni);
                    queue.add(ni);
                    ni.setPrev(currNode);
                }
                currNode.setInfo(VISITED);
            }
        }
        return -1;
    }

    /**
     * Help function: after shortestPathDist checked that there's a valid path and updated the previous vertices,
     * passes through the previous vertex dest to vertex src.
     * Returns a sorted list of nodes from src to dest.
     *
     * @param src
     * @param dest
     * @param path
     * @return List<node_data>
     */
    private List<node_data> shortestPath(int src, int dest, LinkedList<node_data> path) {
        path.addFirst(algorithm.getNode(dest));
        if (src == dest) return path;
        Node_Algo prev = (Node_Algo) algorithm.getNode(dest);
        while (prev.getPrev() != null) {
            prev = (Node_Algo) prev.getPrev();
            path.addFirst(prev);
        }
        return path;
    }

    /**
     * Generates a deep copy of the basic graph to graph - algorithm.
     *
     */
    private void createGraphAlgorithm() {
        algorithm = new DWGraph_DS();
        for (node_data n : graph.getV()) {
            Node_Algo v = new Node_Algo(n);
            v.setPrice(MAX_VALUE);
            v.setInfo(NOT_VISITED);
            algorithm.addNode(v);
        }
        for (node_data n : graph.getV()) {
            for (edge_data e : graph.getE(n.getKey())) {
                algorithm.connect(new Edge(e));
            }
        }
    }


    //////////Private Class  //////////

    /**
     * Inner class: converts a JSON format to a graph.
     */
    private class DWGraph_DSJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

        /**
         * @param jsonElement
         * @param type
         * @param jsonDeserializationContext
         * @return
         * @throws JsonParseException
         */
        @Override
        public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.getAsJsonObject("V") != null) {
                return readMyGraph(jsonElement.getAsJsonObject());
            }
            directed_weighted_graph graph = new DWGraph_DS();
            JsonArray Nodes = jsonObject.getAsJsonArray("Nodes");
            Iterator<JsonElement> itr = Nodes.iterator();
            while (itr.hasNext()) {
                JsonObject object = itr.next().getAsJsonObject();
                String pos = object.getAsJsonObject().get("pos").getAsJsonPrimitive().getAsString();
                double[] loc = simplifyLocation(pos);
                double x = loc[0], y = loc[1], z = loc[2];
                geo_location location = new Location(x, y, z);
                int key = object.getAsJsonObject().get("id").getAsJsonPrimitive().getAsInt();
                graph.addNode(new Node(key, location));
            }
            JsonArray Edges = jsonObject.getAsJsonArray("Edges");
            itr = Edges.iterator();
            while (itr.hasNext()) {
                JsonObject object = itr.next().getAsJsonObject();
                int src = object.getAsJsonObject().get("src").getAsJsonPrimitive().getAsInt(),
                        dest = object.getAsJsonObject().get("dest").getAsJsonPrimitive().getAsInt();
                double weight = object.getAsJsonObject().get("w").getAsJsonPrimitive().getAsDouble();
                ((DWGraph_DS) graph).connect(new Edge(src, dest, weight));
            }
            return graph;
        }

        /**
         * @param jsonElement
         * @return DWGraph_DS
         */
        private directed_weighted_graph readMyGraph(JsonElement jsonElement) {
            directed_weighted_graph graph = new DWGraph_DS();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject V = jsonObject.getAsJsonObject("V").getAsJsonObject();
            for (Map.Entry<String, JsonElement> set : V.entrySet()) {
                JsonElement jsonValue = set.getValue();
                JsonElement l = jsonValue.getAsJsonObject().get("location");
                double x = l.getAsJsonObject().get("_x").getAsDouble(),
                        y = l.getAsJsonObject().get("_y").getAsDouble(),
                        z = l.getAsJsonObject().get("_z").getAsDouble();
                geo_location location = new Location(x, y, z);
                int key = jsonValue.getAsJsonObject().get("key").getAsInt(),
                        tag = jsonValue.getAsJsonObject().get("tag").getAsInt();
                double weight = jsonValue.getAsJsonObject().get("weight").getAsDouble();
                String info = jsonValue.getAsJsonObject().get("info").getAsString();
                graph.addNode(new Node(key, location, weight, info, tag));
            }
            jsonObject = jsonElement.getAsJsonObject();
            JsonObject E = jsonObject.getAsJsonObject("E").getAsJsonObject();
            for (Map.Entry<String, JsonElement> set : E.entrySet()) {
                JsonElement jsonValue = set.getValue();
                for (Map.Entry<String, JsonElement> value : jsonValue.getAsJsonObject().entrySet()) {
                    JsonElement edge = value.getValue();
                    int src = edge.getAsJsonObject().get("src").getAsInt(),
                            dest = edge.getAsJsonObject().get("dest").getAsInt();
                    double weight = edge.getAsJsonObject().get("weight").getAsDouble();
                    ((DWGraph_DS) graph).connect(new Edge(src, dest, weight));
                }
            }
            return graph;
        }

        /**
         * @param s
         * @return double[]
         */
        private double[] simplifyLocation(String s) {
            String[] a = s.split(",");
            double[] d = new double[3];
            d[0] = parseDouble(a[0]);
            d[1] = parseDouble(a[1]);
            d[2] = parseDouble(a[2]);
            return d;
        }
    }

    /**
     * Node_Algo - A private inner class extending Node class with additional methods
     * of a vertex which are used in the set of algorithms.
     */
    private class Node_Algo extends Node implements Comparable<node_data> {
        private double price;
        private node_data prev;

        /**
         * Constructor:
         *
         * @param n
         */
        public Node_Algo(node_data n) {
            super(n);
        }

        /**
         * Compares between two vertices.
         *
         * @param o
         * @return int
         */
        @Override
        public int compareTo(node_data o) {
            Double c = getPrice();
            Node_Algo n = (Node_Algo) o;
            return c.compareTo(n.getPrice());
        }

        /**
         * Returns the price.
         *
         * @return double
         */
        public double getPrice() {
            return price;
        }

        /**
         * Sets the price.
         *
         * @param price
         */
        public void setPrice(double price) {
            this.price = price;
        }

        /**
         * Returns the previous vertex.
         *
         * @return node_data
         */
        public node_data getPrev() {
            return prev;
        }

        /**
         * Sets the previous vertex.
         *
         * @param prev
         */
        public void setPrev(node_data prev) {
            this.prev = prev;
        }
    }
}

