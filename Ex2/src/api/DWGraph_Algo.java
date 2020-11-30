package api;


import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.parseDouble;

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

        if (graph == null) return false;
        if (graph.getV().isEmpty() || graph.nodeSize() == 1) return true;

        DWGraph_DS forward = (DWGraph_DS) copy();
        if (!direction(forward)) return false;

        DWGraph_DS backwards = (DWGraph_DS) forward.reverse();
        if (!direction(backwards)) return false;

        return true;
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        if (graph.getNode(src) == null || graph.getNode(dest) == null)
            throw new RuntimeException("Err:Invalid search src or dest not exists in base graph");

        if (src == dest) return 0;
        for (node_data n : graph.getV()) {
            Node v=(Node)n;
            v.setPrice(MAX_VALUE);
            n.setInfo(NOT_VISITED);
        }
        Node s = (Node) graph.getNode(src);
        s.setPrice(0);
        s.setPrev(null);

        return shortestPathDist(src, dest, new PriorityBlockingQueue<node_data>(graph.getV()));

    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {

        if(shortestPathDist(src,dest)==-1)return null;
        return shortestPath(src,dest,new LinkedList<node_data>());
    }

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

    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_DSJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            graph = gson.fromJson(reader, DWGraph_DS.class);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String toString() {
        return graph.toString();
    }

    public boolean equals(Object obj) {
        return graph.equals(obj);
    }

    ///////// Private Methods /////////
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

    private double shortestPathDist(int src, int dest, PriorityBlockingQueue<node_data> queue) {
        while (!queue.isEmpty()) {
            Node currNode = (Node) queue.remove();
            if (currNode.getKey() == dest || currNode.getPrice() == MAX_VALUE) {
                return currNode.getPrice() == MAX_VALUE ? -1 : currNode.getPrice();
            }
            for (edge_data e : graph.getE(currNode.getKey())) {
                Node ni = (Node) graph.getNode(e.getDest());
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
    private  List<node_data> shortestPath(int src, int dest,LinkedList<node_data> path){
        path.addFirst(graph.getNode(dest));
        if(src==dest)return path;
        Node prev=(Node)graph.getNode(dest);
        while(prev.getPrev()!=null){
            prev=(Node)prev.getPrev();
            path.addFirst(prev);
        }
        return path;
    }

    /////Ptivate Class ////////
    private class DWGraph_DSJsonDeserializer implements JsonDeserializer<DWGraph_DS> {

        @Override
        public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if(jsonObject.getAsJsonObject("V")!=null){
                return readMyGraph(jsonElement.getAsJsonObject());
            }
            DWGraph_DS graph = new DWGraph_DS();
            JsonArray Nodes = jsonObject.getAsJsonArray("Nodes");
            Iterator<JsonElement> itr=Nodes.iterator();
            while(itr.hasNext()){
                JsonObject object = itr.next().getAsJsonObject();
                String pos = object.getAsJsonObject().get("pos").getAsJsonPrimitive().getAsString();
                String[] loc=simplipyLocation(pos);
                double x=parseDouble(loc[0]),y=parseDouble(loc[1]),z=parseDouble(loc[2]);
                geo_location location =new Location(x,y,z);
                int key=object.getAsJsonObject().get("id").getAsJsonPrimitive().getAsInt();
                graph.addNode(new Node(key,location));
            }
            JsonArray Edges = jsonObject.getAsJsonArray("Edges");
            itr=Edges.iterator();
            while(itr.hasNext()){
                JsonObject object = itr.next().getAsJsonObject();
                int src=object.getAsJsonObject().get("src").getAsJsonPrimitive().getAsInt(),
                   dest=object.getAsJsonObject().get("dest").getAsJsonPrimitive().getAsInt();
                  double weight=object.getAsJsonObject().get("w").getAsJsonPrimitive().getAsDouble();
               graph.connect(src,dest,weight);
            }
            return graph;
        }
        private DWGraph_DS readMyGraph(JsonElement jsonElement){
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
                node_data n = new Node(key, location, weight, info, tag);
                graph.addNode(n);
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
                    graph.connect(src, dest, weight);
                }
            }
            return (DWGraph_DS) graph;
        }
        private String[] simplipyLocation(String s){
            return s.split(",");
        }
        }
    }

