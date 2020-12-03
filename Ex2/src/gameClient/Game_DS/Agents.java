package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Double.parseDouble;

public class Agents {
    private final String isEmpty="{\"Agents\":[";
    private game_service game;
    private HashMap<Integer,Agent> agents;
public Agents(game_service game) throws JSONException {
    this.game=game;
    update();
}
public void update() throws JSONException {
    if(agents==null) agents=new HashMap<>();

    if(game.getAgents().equals(isEmpty))return;

    JSONObject jsonAgents=new JSONObject(game.getAgents());
    JSONArray arrayAgents=jsonAgents.getJSONArray("Agents");
    for(int i=0;i<size();i++){
        JSONObject jsonAgent=arrayAgents.getJSONObject(i).getJSONObject("Agent");
        agents.put(jsonAgent.getInt("id"),new Agent(jsonAgent));
    }
}
public int size() throws JSONException {
        JSONObject g=new JSONObject(game.toString());
        JSONObject p=g.getJSONObject("GameServer");
        return p.getInt("agents");
}
public Iterator<Agent> iterator(){
    return agents.values().iterator();
}
public synchronized Agent getAgent(int key){return agents.get(key);}
public String toString(){
    StringBuilder sb=new StringBuilder();
    for(Integer i: agents.keySet()){
        sb.append(agents.get(i).toString());
    }
    return ""+sb;
}
 private class Agent{
    private Integer id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private Point3D pos;
    private edge_data edge;
    private node_data node;

    public Agent(JSONObject json) throws JSONException {
        update(json);
    }
    public void update(JSONObject json) throws JSONException {
        id= json.getInt("id");
        src= json.getInt("src");
        dest= json.getInt("dest");
        value= json.getDouble("value");
        speed= json.getDouble("speed");
        double[] c=simplifyLocation(json.getString("pos"));
        pos=new Point3D(c[0],c[1],c[2]);
        //edge->we need think
        //node->we need think
    }
    private double[] simplifyLocation(String s){
        double[] ans=new double[3];
        String[] as=s.split(",");
        ans[0]=parseDouble(as[0]);
        ans[1]=parseDouble(as[1]);
        ans[2]=parseDouble(as[2]);
        return ans;
    }

     public Integer getId() {return id;}

     public node_data getNode() {return node;}

     public double getValue() {return value;}

     public int getSrc() {return src;}

     public int getDest() {return dest;}

     public double getSpeed() {return speed;}

     public Point3D getPos() {return pos;}

     public edge_data getEdge() {return edge;}
     public boolean isMoving(){ return dest!=-1;}

     public String toString(){
        return "Agent:{"
                +"id:"+getId()
                +",src:"+getSrc()
                +",dest:"+getDest()
                +",pos:"+getPos().toString()
                +"}";
    }
 }
}
