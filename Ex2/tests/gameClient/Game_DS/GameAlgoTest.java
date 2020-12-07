package gameClient.Game_DS;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameAlgoTest {

    @Test
    void Constructor() throws JSONException {
        game_service game = Game_Server_Ex2.getServer(11);
        DWGraph_Algo algo=  new DWGraph_Algo();
        directed_weighted_graph graph=algo.readFromJson(game.getGraph());
        algo.init(graph);
        Agents a=new Agents(game);
        Pokemons p= new Pokemons(game);
        GameAlgo gameAlgo=new GameAlgo(game,algo,p,a);
int y=0;


    }
}