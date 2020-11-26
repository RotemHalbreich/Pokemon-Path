package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class represents a directional weighted graph.
 * The interface has a road-system or communication network in mind -
 * and should support a large number of nodes (over 100,000).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 */

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> graph;
    private HashMap<Integer, node_data> R = new HashMap<>();
    private int EDGES = 0;
    private int MC = 0;

    public DWGraph_DS() {
        graph = new HashMap<Integer, node_data>();
    }

    /**
     * returns the node_data by the node_id
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (graph.containsKey(key))
            return graph.get(key);
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return edge_data
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        Node v = (Node) getNode(src);
        if (v != null) return v.get(dest);
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (graph.containsKey(n.getKey()))
            throw new RuntimeException("Err: This vertex already exists");
        graph.put(n.getKey(), n);
        R.put(n.getKey(), new Node(n));
        MC++;
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (w < 0) throw new RuntimeException("Err:Invalid value w=" + w + "can't be negative number");
        if (!graph.containsKey(src) || !graph.containsKey(dest))
            throw new RuntimeException("Err:Invalid insert src or dest doesn't exists in base graph");
        if (src == dest)
            throw new RuntimeException("Err: Invalid insert, src and dest are equals!");
        Node v = (Node) getNode(src);
        if (!v.containsKey(dest)) EDGES++;
        edge_data e = new Edge(src, dest, w);
        v.put(dest, e);
        transposeConnect(src, dest, w);
        MC++;
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return graph.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!graph.containsKey(node_id))
            throw new RuntimeException("Err:Invalid search node_id=" + node_id + "doesn't exists in base graph");
        Node v = (Node) getNode(node_id);
        return v.values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (graph.containsKey(key)) {
            Collection<edge_data> ni = getE(key);
            Node r = (Node) R.get(key);
            EDGES -= ni.size();
            MC += ni.size() + 1;

            for (edge_data e : r.values())
                removeEdges(e.getDest(), key);

            for (edge_data e : ni) {
                Node v = (Node) R.get(e.getDest());
                v.remove(e.getSrc());
            }
        }
        R.remove(key);
        return graph.remove(key);
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (graph.containsKey(src) && graph.containsKey(dest)) {
            Node v = (Node) getNode(src);
            if (v.containsKey(dest)) {
                MC++;
                EDGES--;
            }
            Node r = (Node) R.get(dest);
            r.remove(src);
            return v.remove(dest);
        }
        return null;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return int
     */
    @Override
    public int nodeSize() {
        return graph.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return EDGES
     */
    @Override
    public int edgeSize() {
        return EDGES;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return MC
     */
    @Override
    public int getMC() {
        return MC;
    }

    public HashMap<Integer, node_data> reverse() {
        return R;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        directed_weighted_graph g = new DWGraph_DS();
        if (obj instanceof directed_weighted_graph)
            g = (directed_weighted_graph) obj;
        if (obj instanceof dw_graph_algorithms) {
            g = ((dw_graph_algorithms) obj).getGraph();
        }
        if(g.getV().isEmpty()&&getV().isEmpty())return true;
        if(edgeSize()!=g.edgeSize()||nodeSize()!=g.nodeSize())
            return false;

        return similar(this,g)&&similar(g,this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, R, EDGES, MC);
    }

    /**
     * Returns the graph as a String
     *
     * @return String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : graph.keySet()) {
            Node v = (Node) getNode(i);
            for (edge_data e : v.values()) {
                sb.append(e.toString());
            }
        }
        return "V:" + graph.keySet() + "\nE:" + sb + "\n";
    }

    /**
     * Returns the graph as a reversed String
     *
     * @return String
     */
    public String toStringReverse() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : R.keySet()) {
            Node v = (Node) R.get(i);
            for (edge_data e : v.values()) {
                sb.append(e.toString());
            }
        }
        return "V:" + R.keySet() + "\nE:" + sb + "\n";
    }

    /////////// PRIVATE METHOD //////////
    private void transposeConnect(int src, int dest, double w) {
        Node r = (Node) R.get(dest);
        edge_data er = new Edge(dest, src, w);
        r.put(src, er);
    }

    private edge_data removeEdges(int src, int dest) {
        Node v = (Node) getNode(src);
        if (v.containsKey(dest)) {
            MC++;
            EDGES--;
        }
        return v.remove(dest);
    }

    private boolean similar(directed_weighted_graph g1, directed_weighted_graph g2) {
        for(node_data n:g1.getV()){
            if(!n.equals(g2.getNode(n.getKey())))
                return false;
        }
        for(node_data n:g1.getV()){
            for(edge_data e:g1.getE(n.getKey())){
                try{
                    if(!e.equals(g2.getEdge(e.getSrc(),e.getDest())))
                        return false;
                }catch (RuntimeException r){
                    return false;
                }
            }
        }
        return true;

    }
}
