package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> graph;
    private HashMap<Integer, node_data> R=new HashMap<>();
    private int EDGES = 0;
    private int MC = 1;

    public DWGraph_DS() {
        graph = new HashMap<Integer, node_data>();
    }

    @Override
    public node_data getNode(int key) {
        if (graph.containsKey(key))
            return graph.get(key);
        return null;
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        Node v = (Node) getNode(src);
        if (v != null) return v.get(dest);
        return null;
    }

    @Override
    public void addNode(node_data n) {
        if (graph.containsKey(n.getKey()))
            throw new RuntimeException("Err: This vertex already exists");
        graph.put(n.getKey(), n);
        R.put(n.getKey(),new Node(n));
        MC++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (w < 0) throw new RuntimeException("Err:Invalid value w=" + w + "can't be negative number");
        if (!graph.containsKey(src) || !graph.containsKey(dest))
            throw new RuntimeException("Err:Invalid insert src or dest doesn't exists in base graph");
        if(src==dest)
            throw new RuntimeException("Err: Invalid insert, src and dest are equals!");
        Node v = (Node) getNode(src);
        if (!v.containsKey(dest)) EDGES++;
        edge_data e = new Edge(src, dest, w);
        v.put(dest, e);
        transposeConnect(src,dest,w);
        MC++;

    }

    @Override
    public Collection<node_data> getV() {
        return graph.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!graph.containsKey(node_id))
            throw new RuntimeException("Err:Invalid search node_id=" + node_id + "doesn't exists in base graph");
        Node v = (Node) getNode(node_id);
        return v.values();
    }

    @Override
    public node_data removeNode(int key) {
        if (graph.containsKey(key)) {
            Collection<edge_data> ni=getE(key);
            Node r=(Node)R.get(key);

            for(edge_data e:r.values())
                removeEdges(e.getDest(),key);

            for(edge_data e:ni){
                Node v=(Node)R.get(e.getDest());
                v.remove(e.getSrc());
            }
        }
        R.remove(key);
        return graph.remove(key);
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if (graph.containsKey(src) && graph.containsKey(dest)) {
            Node v = (Node)getNode(src);
            if (v.containsKey(dest)) {
                MC++;
                EDGES--;
            }
            Node r=(Node)R.get(dest);
            r.remove(src);
            return v.remove(dest);
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return graph.size();
    }

    @Override
    public int edgeSize() {
        return EDGES;
    }

    @Override
    public int getMC() {
        return MC;
    }
    public HashMap<Integer, node_data> reverse(){return R;}
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : graph.keySet()) {
            Node v = (Node) getNode(i);
            for (edge_data e : v.values()) {
                sb.append(e.toString());
            }
        }

        return "V:" + graph.keySet() + "\nE:" + sb;
    }
    public String toStringReverse() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : R.keySet()) {
            Node v = (Node) R.get(i);
            for (edge_data e : v.values()) {
                sb.append(e.toString());
            }
        }

        return "V:" + R.keySet() + "\nE:" + sb;
    }
   /////////// PRIVATE METHOD //////////
    private void transposeConnect(int src,int dest,double w){
        Node r=(Node)R.get(dest);
        edge_data er=new Edge(dest,src,w);
        r.put(src,er);
    }

    private edge_data removeEdges(int src, int dest) {
            Node v = (Node)getNode(src);
            if (v.containsKey(dest)) {
                MC++;
                EDGES--;
            }
            return v.remove(dest);
    }

}
