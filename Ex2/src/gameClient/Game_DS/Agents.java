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
public synchronized void update() throws JSONException {
    if(agents==null) agents=new HashMap<>();

    if(game.getAgents().equals(isEmpty))return;

    JSONObject jsonAgents=new JSONObject(game.getAgents());
    JSONArray arrayAgents=jsonAgents.getJSONArray("Agents");
    for(int i=0;i<size();i++){

        JSONObject jsonAgent=arrayAgents.getJSONObject(i).getJSONObject("Agent");

        int id=jsonAgent.getInt("id");

        if(agents.containsKey(id))
            agents.get(id).update(jsonAgent);
        else
            agents.put(id,new Agent(jsonAgent));
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

}
