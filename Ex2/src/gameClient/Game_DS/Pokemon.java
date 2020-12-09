package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

public class Pokemon implements Comparable<Pokemon> {
    private game_service game;
    private double value;
    private int type;
    private Point3D pos;
    private edge_data edge;
    private final double EPS=0.0000000000001;


    public Pokemon(JSONObject json,game_service game) throws JSONException {
        this.game=game;
        update(json);
    }

    public void update(JSONObject json) throws JSONException {
        value = json.getDouble("value");
        type = json.getInt("type");
        double[] c = simplifyPos(json.getString("pos"));
        pos = new Point3D(c[0], c[1], c[2]);
        setEdge();
    }

    public double[] simplifyPos(String s) {
        double[] ans = new double[3];
        String[] as = s.split(",");
        ans[0] = parseDouble(as[0]);
        ans[1] = parseDouble(as[1]);
        ans[2] = parseDouble(as[2]);
        return ans;
    }

    public double getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public Point3D getLocation() {
        return pos;
    }

    public void setEdge() {
        edge = null;
        directed_weighted_graph graph=new DWGraph_Algo().readFromJson(game.getGraph());
        for(node_data n:graph.getV()){
            for (edge_data e:graph.getE(n.getKey())){
                if(isValidEdge(e)){
                    Point3D src= (Point3D) graph.getNode(e.getSrc()).getLocation();
                    Point3D dest= (Point3D) graph.getNode(e.getDest()).getLocation();
                    double d=src.distance(dest)- src.distance(getLocation())-getLocation().distance(dest);
                    if(Math.abs(d)<EPS)
                        edge=e;
                }
            }
        }

    }

    public synchronized edge_data getEdge() {
        return edge;
    }


    public String toString() {
        return "Pokemon:" +
                "{ "
                + "val:" + getValue()
                + ",type:" + getType()
                + ",pos:" + getLocation().toString()
                + ",edge:"+getEdge().toString()
                + "}";
    }

    @Override
    public int compareTo(@NotNull Pokemon o) {
        Double v = getValue();
        return -v.compareTo(o.value);
    }
    ///////// Private Methods /////////
    private boolean isValidEdge(edge_data e){
        return getType()>0&&e.getSrc()>e.getDest()||
            getType()<0&&e.getSrc()<e.getDest();

    }

}
