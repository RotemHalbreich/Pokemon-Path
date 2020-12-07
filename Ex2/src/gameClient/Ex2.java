package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import api.directed_weighted_graph;
import api.game_service;
import gameClient.Game_DS.Agents;
import gameClient.Game_DS.GameAlgo;
import gameClient.Game_DS.Pokemons;
import org.json.JSONException;

public class Ex2 {
    public static void main(String[] args) throws JSONException {

        game_service game = Game_Server_Ex2.getServer(1);

        DWGraph_Algo algo=  new DWGraph_Algo();
        directed_weighted_graph graph=algo.readFromJson(game.getGraph());
        algo.init(graph);
        Agents a=new Agents(game);
        Pokemons p= new Pokemons(game);
        GameAlgo gameAlgo=new GameAlgo(game,algo,p,a);


        gameAlgo.start();

    }
}
