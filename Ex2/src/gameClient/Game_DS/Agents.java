package gameClient.Game_DS;

import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Double.MAX_VALUE;

/**
 * Represents the list of the game's Agents.
 *
 * game - the game server.
 * info - the game's information.
 * agents - the game's Agents.
 * dps - a matrix holds the shortest paths between a node to any other node.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Agents {

    private final String isEmpty = "{\"Agents\":[";
    private game_service game;
    private Information info;
    private HashMap<Integer, Agent> agents;
    private double[][] dps;
    private double[][] dps1;

    /**
     * Constructor:
     *
     * @param game
     * @param i
     * @throws JSONException
     */
    public Agents(game_service game,Information i) throws JSONException {
        this.game = game;
        this.info=i;
        update();
        insertDPS();
        insertDPS1();

    }

    /**
     *
     * Inserts the DPS in the minimum distances.
     */
    private void insertDPS1() {
        directed_weighted_graph g=new DWGraph_Algo().readFromJson(game.getGraph());
        int s=findMaxId(g);
        if(s==-1||s==0)return;
        dps1=new double[s][s];
        setMinPath(g);
    }

    /**
     * Calculates the shortest path distance between a vertex to any other vertex.
     *
     * @param g
     */
    private void setMinPath(directed_weighted_graph g) {
        DWGraph_Algo algo=new DWGraph_Algo();
        algo.init(g);
        for(node_data n:g.getV()){
            for (node_data n1:g.getV()){
                dps1[n.getKey()][n1.getKey()]=algo.shortestPathDist(n.getKey(),n1.getKey());
            }
        }
    }

    /**
     * Updates the agents.
     *
     * @throws JSONException
     */
    public synchronized void update() throws JSONException {

        if(game==null||info==null)return;

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

    /**
     * Returns the amount of agents in the game.
     *
     * @return int
     * @throws JSONException
     */
    public int size() throws JSONException {
       return info.getAgents();
    }

    /**
     * Returns an iterator of agents.
     *
     * @return Iterator<Agent>
     */
    public Iterator<Agent> iterator() {
        return agents.values().iterator();
    }

    /**
     * Returns an agent by a unique key.
     *
     * @param key
     * @return Agent
     */
    public synchronized Agent getAgent(int key) {
        return agents.get(key);
    }

    public  double DPS(int key,int i ,int j){
        return (dps[i][j]);
    }

    /**
     * Returns the minimal distance between src to dest.
     *
     * @param src
     * @param dest
     * @return double
     */
    public double DPS1 (int src,int dest){
        double ans=dps1[src][dest];
        return ans>=0?ans:MAX_VALUE;
    }

    /**
     * Returns the agents as a String.
     *
     * @return String.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : agents.keySet()) {
            sb.append(agents.get(i).toString());
        }
        return "" + sb;
    }

    /////////// Private Methods //////////

    /**
     *
     */
    private void insertDPS() {
        directed_weighted_graph g=new DWGraph_Algo().readFromJson(game.getGraph());
        int s=findMaxId(g);
        if(s==-1||s==0)return;
        dps=new double[s][s];
        insertDefaultValue(g,s);
        insertActualValue(g);
        minDPS();
    }

    /**
     * Finds the maximum key in case the vertices' keys are note arranged serially.
     *
     * @param g
     * @return int
     */
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
