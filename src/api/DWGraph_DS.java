package api;


import java.util.*;

/**
 * This class represents a directional weighted graph.
 * V - represents the vertices of the graph.
 * E - represents the edges of the graph.
 * R - represents the reversed graph.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> V;
    private HashMap<Integer, HashMap<Integer, edge_data>> E;
    private HashMap<Integer, HashMap<Integer, edge_data>> R = new HashMap<>();
    private int MC = 0;
    private int EDGES = 0;

    /**
     * Constructor:
     */
    public DWGraph_DS() {
        V = new HashMap<Integer, node_data>();
        E = new HashMap<Integer, HashMap<Integer, edge_data>>();
    }

    /**
     * Returns the vertex by a unique key (ID).
     *
     * @param key - the vertex's ID
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return V.get(key);
    }

    /**
     * Returns the data of the edge (src,dest), null if none.
     *
     * @param src
     * @param dest
     * @return edge_data
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (E.containsKey(src))
            return E.get(src).get(dest);
        return null;
    }

    /**
     * Adds a new vertex to the graph with the given node_data.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (V.containsKey(n.getKey()))
            throw new RuntimeException("Err: Invalid insert n already exists");
        V.put(n.getKey(), n);
        MC++;
    }

    /**
     * Connects an edge with weight between vertex src to vertex dest
     * while both of the vertices exist.
     *
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     * @throws RuntimeException - if (w < 0 || one of the vertices doesn't exist in the graph)
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (w < 0)
            throw new RuntimeException("Err: Invalid value weight can't be negative number");
        if (!V.containsKey(src) || !V.containsKey(dest))
            throw new RuntimeException("Err: Invalid insert src or dest doesn't exists");

        if (E.containsKey(src)) {
            if (!E.get(src).containsKey(dest)) EDGES++;
            E.get(src).put(dest, new Edge(src, dest, w));
        } else {
            EDGES++;
            HashMap<Integer, edge_data> e = new HashMap<>();
            e.put(dest, new Edge(src, dest, w));
            E.put(src, e);
        }
        reverseConnect(dest, src, w);
        MC++;
    }

    /**
     * Connects an edge to the graph.
     *
     * @param e
     */
    public void connect(edge_data e) {
        connect(e.getSrc(), e.getDest(), e.getWeight());
    }

    /**
     * Returns a shallow copy of the collection representing all the vertices in the graph.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return V.values();
    }

    /**
     * Returns a shallow copy of the collection representing all the edges
     * connected to the given vertex, while this given vertex is the src of all its edges.
     * If the vertex's neighbors don't exist, return an empty list.
     *
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (E.containsKey(node_id))
            return E.get(node_id).values();
        return new LinkedList<>();
    }

    /**
     * Deletes the vertex by its unique key from the graph,
     * then removes all the edges connected to this vertex.
     *
     * @return node_data - the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_data removeNode(int key) {
        if (R.containsKey(key)) {
            Collection<edge_data> ni = R.get(key).values();
            for (edge_data e : ni)
                removeEdges(e.getDest(), e.getSrc());
            for (edge_data e : getE(key))
                reverseRemove(e.getDest(), e.getSrc());
        }
        R.remove(key);
        EDGES -= getE(key).size();
        MC += getE(key).size() + 1;
        E.remove(key);
        return V.remove(key);
    }

    /**
     * Deletes the edge from the graph.
     *
     * @param src
     * @param dest
     * @return edge_data - the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (E.containsKey(src)) {
            EDGES--;
            MC++;
            reverseRemove(dest, src);
            return E.get(src).remove(dest);
        }
        return null;
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return int
     */
    @Override
    public int nodeSize() {
        return V.size();
    }

    /**
     * Returns the number of edges in the graph.
     *
     * @return int
     */
    @Override
    public int edgeSize() {
        return EDGES;
    }

    /**
     * Returns the Mode Count which represents the amount of changes made to the graph.
     *
     * @return int
     */
    @Override
    public int getMC() {
        return MC;
    }

    /**
     * Returns the graph as a String.
     *
     * @return String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : E.keySet()) {
            if (!getE(i).isEmpty())
                sb.append(E.get(i).values().toString());
        }
        return "V:" + V.keySet() + "\n" + "E:" + sb + "\n";
    }

    /**
     * Returns the graph as a reversed String.
     *
     * @return String
     */
    public String reverseString() {
        return reverse().toString();
    }

    /**
     * hashCode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(V, E, R, MC, EDGES);
    }

    /**
     * Compare between two graphs.
     * returns true iff the graphs are logically equals --> else false
     *
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        directed_weighted_graph g = new DWGraph_DS();

        if (obj instanceof directed_weighted_graph)
            g = (directed_weighted_graph) obj;

        if (obj instanceof dw_graph_algorithms)
            g = ((dw_graph_algorithms) obj).getGraph();

        if (nodeSize() != g.nodeSize() || edgeSize() != g.edgeSize()) return false;

        return similar(this, g) && similar(g, this);
    }

    /**
     * Returns a deep copy of the reversed graph.
     * Used in DWGraph_Algo.
     *
     * @return directed_weighted_graph
     */
    public directed_weighted_graph reverse() {
        directed_weighted_graph r = new DWGraph_DS();
        for (Integer n : R.keySet()) r.addNode(new Node(getNode(n)));
        for (Integer i : R.keySet()) {
            for (edge_data e : R.get(i).values()) {
                edge_data edge = new Edge(e);
                r.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
            }
        }
        return r;
    }

    ////////// Private Methods //////////

    /**
     * This private method connects between two vertices with an edge in reverse.
     *
     * @param src
     * @param dest
     * @param w
     */
    private void reverseConnect(int src, int dest, double w) {
        if (R.containsKey(src)) {
            R.get(src).put(dest, new Edge(src, dest, w));
        } else {
            HashMap<Integer, edge_data> e = new HashMap<>();
            e.put(dest, new Edge(src, dest, w));
            R.put(src, e);
        }
    }

    /**
     * This private method checks if two different graphs are identical,
     * aka if every single vertex and edge exist in both graphs.
     *
     * @param g1 - first graph
     * @param g2 - second graph
     * @return boolean
     */
    private boolean similar(directed_weighted_graph g1, directed_weighted_graph g2) {
        for (node_data n : g1.getV()) {
            if (g2.getNode(n.getKey()) == null)
                return false;
            for (edge_data e : g1.getE(n.getKey())) {
                if (g2.getEdge(e.getSrc(), e.getDest()) == null || !e.equals(g2.getEdge(e.getSrc(), e.getDest())))
                    return false;
            }
        }
        return true;
    }

    /**
     * This private method deletes an edge from the reversed graph.
     *
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     */
    private void reverseRemove(int src, int dest) {
        R.get(src).remove(dest);
    }

    /**
     * This private method deletes edges from the graph.
     *
     * @param src
     * @param dest
     */
    private void removeEdges(int src, int dest) {
        if (E.containsKey(src)) {
            EDGES--;
            MC++;
            E.get(src).remove(dest);
        }
    }
}
