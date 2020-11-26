package api;

import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {
    private static final String NOT_VISITED = "white", VISITED = "green", FINISH = "black";
    private directed_weighted_graph graph;

    public DWGraph_Algo() {
        graph = new DWGraph_DS();
    }

    @Override
    public void init(directed_weighted_graph g) {
        graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph copy = new DWGraph_DS();
        for (node_data n : graph.getV())
            copy.addNode(new Node(n));

        for (node_data n : graph.getV()) {
            for (edge_data e : graph.getE(n.getKey())) {
                edge_data edge = new Edge(e);
                copy.connect(edge.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return copy;
    }

    @Override
    public boolean isConnected() {
        if(graph==null)return false;
        if(graph.getV().isEmpty()||graph.nodeSize()==1)return true;

        DWGraph_DS forward= (DWGraph_DS) copy();
        if (!direction(forward)) return false;

        DWGraph_DS backwards= (DWGraph_DS) forward.reverse();
        if (!direction(backwards)) return false;

        return true;
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
    public boolean equals(Object o){return graph.equals(o);}
    /////////// PRIVATE METHODS /////////
    private void isConnected(directed_weighted_graph g,Node n){
        n.setInfo(VISITED);
        for(Integer i:n.keySet()){
            Node ni=(Node)g.getNode(i);
            if(ni.getInfo().equals(NOT_VISITED))
                isConnected(g,ni);
        }
        n.setInfo(FINISH);
    }

    private boolean direction(DWGraph_DS g) {
        for(node_data n:g.getV())n.setInfo(NOT_VISITED);
        Node first=(Node)g.getV().iterator().next();
        isConnected(g,first);
        for(node_data n:g.getV()){
            if(!n.getInfo().equals(FINISH))
                return false;
        }
        return true;
    }
}
