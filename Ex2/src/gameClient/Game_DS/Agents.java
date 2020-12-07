package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.parseDouble;

public class Agents {
    private final String isEmpty = "{\"Agents\":[";
    private game_service game;
    private HashMap<Integer, Agent> agents;
    private double[][] dps;

    public Agents(game_service game) throws JSONException {
        this.game = game;
        update();
        insertDPS();
    }


    public synchronized void update() throws JSONException {
        if (agents == null) agents = new HashMap<>();

        if (game.getAgents().equals(isEmpty)) return;

        JSONObject jsonAgents = new JSONObject(game.getAgents());
        JSONArray arrayAgents = jsonAgents.getJSONArray("Agents");
        for (int i = 0; i < size(); i++) {

            JSONObject jsonAgent = arrayAgents.getJSONObject(i).getJSONObject("Agent");

            int id = jsonAgent.getInt("id");

            if (agents.containsKey(id))
                agents.get(id).update(jsonAgent);
            else
                agents.put(id, new Agent(jsonAgent));
        }
    }

    public int size() throws JSONException {
        JSONObject g = new JSONObject(game.toString());
        JSONObject p = g.getJSONObject("GameServer");
        return p.getInt("agents");
    }

    public Iterator<Agent> iterator() {
        return agents.values().iterator();
    }

    public synchronized Agent getAgent(int key) {
        return agents.get(key);
    }
    public long DPS(int key,int i ,int j){
        Agent a=getAgent(key);
        return (long)(dps[i][j]/a.getSpeed());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : agents.keySet()) {
            sb.append(agents.get(i).toString());
        }
        return "" + sb;
    }

    /////////// Private Methods //////////
    private void insertDPS() {
        directed_weighted_graph g=new DWGraph_Algo().readFromJson(game.getGraph());
        int s=findMaxId(g);
        if(s==-1||s==0)return;
        dps=new double[s][s];
        insertDefaultValue(g,s);
        insertActualValue(g);
        minDPS();
    }
    private int findMaxId( directed_weighted_graph g){
        int m=-1;
       for (node_data n:g.getV()){
           if(m<n.getKey())
               m=n.getKey();
       }
       return m+1;
    }
    private void insertDefaultValue(directed_weighted_graph g,int s){
        for(int i=0;i<s;i++){
            for(int j=0;j<s;j++){
                if(i==j&&g.getNode(i)!=null)
                    dps[i][j]=0;
                else dps[i][j]=MAX_VALUE;
            }
        }
    }
    private void insertActualValue(directed_weighted_graph g){
        for (node_data n:g.getV()){
            for(edge_data e:g.getE(n.getKey())){
                dps[e.getSrc()][e.getDest()]=e.getWeight();
            }
        }
    }
    private void minDPS(){
        for (int k = 0; k < dps.length; k++) {
            for (int i = 0; i < dps.length; i++) {
                for (int j = 0; j < dps.length; j++) {
                    if (dps[i][j] > dps[i][k] + dps[k][j])
                        dps[i][j] = dps[i][k] + dps[k][j];
                }
            }
        }
    }

}
