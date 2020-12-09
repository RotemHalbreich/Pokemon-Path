package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import api.directed_weighted_graph;
import api.game_service;
import gameClient.Game_DS.*;
import org.json.JSONException;

public class Ex2 {
    public static void main(String[] args) throws JSONException {

        game_service game = Game_Server_Ex2.getServer(11);
        Information i=new Information(game);
        DWGraph_Algo algo=  new DWGraph_Algo();
        directed_weighted_graph graph=algo.readFromJson(game.getGraph());
        algo.init(graph);
        Agents a=new Agents(game,i);
        Pokemons p= new Pokemons(game,i);
        GameAlgo gameAlgo=new GameAlgo(game,i,algo,p,a);

        Thread gameThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (gameAlgo.getGame().isRunning()) {
                        gameAlgo.getPokemons().update();
                        //gameAlgo.getGame().move();
                        gameAlgo.sendAgentsToPokemons();
                        gameAlgo.getAgents().update();
                        gameAlgo.moveAgents();
                        gameAlgo.updateHandled();

                        Thread.sleep(100);
                        System.out.println(game.timeToEnd()/1000);
                        System.out.println(game.toString());
                        System.out.println(game.getAgents());
                    }

                    } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        gameAlgo.startGame();
        System.out.println(game.timeToEnd()/1000);
        gameThread.start();




    }
}
