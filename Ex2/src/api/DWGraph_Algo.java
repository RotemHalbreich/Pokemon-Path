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
        return false;
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
}
