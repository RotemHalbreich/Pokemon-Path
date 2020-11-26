package api;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import static java.lang.Double.MAX_VALUE;

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
        try{graph.getNode(src);graph.getNode(dest);}
        catch (RuntimeException e){e.printStackTrace();}
        if(src==dest)return 0;
        for(node_data n:graph.getV()){
            n.setWeight(MAX_VALUE);
            n.setInfo(NOT_VISITED);
        }
        Node s=(Node)graph.getNode(src);
        s.setWeight(0);
        s.setPrev(null);

        return shortestPathDist(src,dest,new PriorityBlockingQueue<node_data>(graph.getV()));
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
    private double shortestPathDist(int src,int dest,PriorityBlockingQueue<node_data> queue){
        while(!queue.isEmpty()){
            Node currNode= (Node) queue.remove();
            if(currNode.getKey()==dest||currNode.getWeight()==MAX_VALUE){
                return currNode.getWeight()==MAX_VALUE ?-1:currNode.getWeight();
            }
            for(edge_data e:currNode.values()){
                Node ni= (Node) graph.getNode(e.getDest());
                double weight=currNode.getWeight()+e.getWeight();
                if(ni.getInfo().equals(NOT_VISITED)&&ni.getWeight()>weight){
                    ni.setWeight(weight);
                    queue.remove(ni);
                    queue.add(ni);
                    ni.setPrev(currNode);
                }
                currNode.setInfo(VISITED);
            }
        }
        return -1;
    }
}
