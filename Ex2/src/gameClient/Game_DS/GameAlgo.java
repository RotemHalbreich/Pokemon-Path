package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GameAlgo {
    private game_service game;
    private dw_graph_algorithms algo;
    private Pokemons pokemons;
    private Agents agents;
    private List<Point3D> handlingPokemons=new ArrayList<>();
    private HashMap<Integer,ArrayList<node_data>> targets=new HashMap<>();
    private HashMap<Integer,Long> timer=new HashMap<>();
    private double[][] pairDis;

    public GameAlgo(game_service game,dw_graph_algorithms algo,Pokemons p,Agents a) throws JSONException {
        this.game=game;
        this.algo=algo;
        this.pokemons=p;
        this.agents=a;
        insertPairDist();
        insertFirstTime();
        agents.update();
        insertTargetsAndTime();

    }

    private void insertTargetsAndTime() {
        Iterator<Agent> itr=agents.iterator();
        while (itr.hasNext()){
            ArrayList<node_data> t=new ArrayList<>();
            Agent a= itr.next();
            t.add(algo.getGraph().getNode(a.getSrc()));
            targets.put(a.getId(),t);
            timer.put(a.getId(), 0L);
        }


    }

    private void insertFirstTime() throws JSONException {
        Iterator<Pokemon> itr=pokemons.iterator();
        for(int i=0;i<agents.size();i++){
            if(itr.hasNext()){
                int src=itr.next().getEdge().getSrc();
                game.addAgent(src);
            }
            else{
                int r=algo.getGraph().nodeSize();
                game.addAgent((int)(Math.random()*r));
            }

        }
    }

    private void insertPairDist() {

    }

}
