package api;


import java.util.*;

/**
 * This class represents a directional weighted graph.
 * The interface has a road-system or communication network in mind -
 * and should support a large number of nodes (over 100,000).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 */

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> V;
    private HashMap<Integer, HashMap<Integer, edge_data>> E;
    private HashMap<Integer, HashMap<Integer, edge_data>> R = new HashMap<>();

    private int MC = 0;
    private int EDGES = 0;

    public DWGraph_DS() {
        V = new HashMap<Integer, node_data>();
        E = new HashMap<Integer, HashMap<Integer, edge_data>>();
    }

    @Override
    public node_data getNode(int key) {
        return V.get(key);
    }


    @Override
    public edge_data getEdge(int src, int dest) {
        if (E.containsKey(src))
            return E.get(src).get(dest);
        return null;
    }

    @Override
    public void addNode(node_data n) {
        if (V.containsKey(n.getKey()))
            throw new RuntimeException("Err:Invalid insert n already exists");
        V.put(n.getKey(), n);
        MC++;
    }

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
    public void connect(edge_data e){connect(e.getSrc(),e.getDest(),e.getWeight()); }

    @Override
    public Collection<node_data> getV() {
        return V.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if (E.containsKey(node_id))
            return E.get(node_id).values();
        return new LinkedList<>();
    }

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

    @Override
    public int nodeSize() {return V.size();}

    @Override
    public int edgeSize(){return EDGES;}

    @Override
    public int getMC() {return MC;}

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : E.keySet()) {
            if (!getE(i).isEmpty())
                sb.append(E.get(i).values().toString());
        }
        return "V:" + V.keySet() + "\n" + "E:" + sb + "\n";
    }

    public String reverseString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : R.keySet()) {
            if (!R.get(i).isEmpty())
                sb.append(R.get(i).values().toString());
        }
        return "V_R:" + R.keySet() + "\n" + "E_R:" + sb + "\n";
    }

    @Override
    public int hashCode() {return Objects.hash(V, E, R, MC, EDGES);}

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
    public directed_weighted_graph reverse(){
        directed_weighted_graph r=new DWGraph_DS();
        for(Integer n:R.keySet())r.addNode(new Node(getNode(n)));
        for(Integer i :R.keySet()){
            for(edge_data e:R.get(i).values()){
                edge_data edge=new Edge(e);
                r.connect(edge.getSrc(),edge.getDest(),edge.getWeight());
            }
        }
        return r;
    }

    ////////// Private Methods //////////
    private void reverseConnect(int src, int dest, double w) {
        if (R.containsKey(src)) {
            R.get(src).put(dest, new Edge(src, dest, w));
        } else {
            HashMap<Integer, edge_data> e = new HashMap<>();
            e.put(dest, new Edge(src, dest, w));
            R.put(src, e);
        }
    }

    private boolean similar(directed_weighted_graph g1, directed_weighted_graph g2) {
        for (node_data n : g1.getV()) {
            if (g2.getNode(n.getKey()) == null)
                return false;
            for (edge_data e : g1.getE(n.getKey())) {
                if (g2.getEdge(e.getSrc(), e.getDest()) == null)
                    return false;
            }
        }
        return true;
    }

    private void reverseRemove(int src, int dest) {
        R.get(src).remove(dest);
    }

    private void removeEdges(int src, int dest) {
        if (E.containsKey(src)) {
            EDGES--;
            MC++;
            E.get(src).remove(dest);
        }
    }
}
