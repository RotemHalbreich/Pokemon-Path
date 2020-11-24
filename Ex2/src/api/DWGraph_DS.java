package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer,node_data> graph;
    private int EDGES=0;
    private int MC=1;

    public DWGraph_DS(){
        graph=new HashMap<Integer,node_data>();
    }
    @Override
    public node_data getNode(int key) {
       if(graph.containsKey(key))
           return graph.get(key);
        return null;
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        Node v=(Node)getNode(src);
        if(v!=null) return v.get(dest);
        return null;
    }

    @Override
    public void addNode(node_data n) {
        if(graph.containsKey(n.getKey()))
            throw new RuntimeException("Err: This vertex exists");
        graph.put(n.getKey(),n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(w<0) throw new RuntimeException("Err:Invalid value w="+w+"can't to be a  negative");
        if(!graph.containsKey(src)|| !graph.containsKey(dest))
            throw new RuntimeException("Err:Invalid insert src or dest doesn't exists in base graph");
        Node v=(Node)getNode(src);
        if(!v.containsKey(dest))EDGES++;
        edge_data e=new Edge(src,dest,w);
        v.put(dest,e);

    }

    @Override
    public Collection<node_data> getV() {
        return graph.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if(!graph.containsKey(node_id))
            throw new RuntimeException("Err:Invalid search node_id="+node_id+"doesn't exists in base graph");
        Node v=(Node)getNode(node_id);
        return v.values();
    }

    @Override
    public node_data removeNode(int key) {
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }


}
