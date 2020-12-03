package gameClient.Game_DS;

import gameClient.util.Point3D;
import org.json.JSONObject;

public class Agents {

 private class Agent{
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private Point3D;
    private edge_data edge;
    private node_data node;

    public Agent(JSONObject json){
        update();
    }
 }
}
