package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import api.directed_weighted_graph;
import api.game_service;
import gameClient.Game_DS.*;
import org.json.JSONException;

public class Ex2 implements Runnable {
    private static GameGui2 gui = new GameGui2();
    private static GameAlgo gameAlgo;

    public static void main(String[] args) {
        Thread ex2 = new Thread(new Ex2());
        ex2.start();
    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(11);
        DWGraph_Algo algo = null;
        Information i = null;
        Agents a = null;
        Pokemons p = null;
        directed_weighted_graph graph = null;
        try {
            i = new Information(game);
            algo = new DWGraph_Algo();
            graph = algo.readFromJson(game.getGraph());
            algo.init(graph);
            a = new Agents(game, i);
            p = new Pokemons(game, i);
            gameAlgo = new GameAlgo(game, i, algo, p, a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gui.update(gameAlgo);
       // gui.show();
        gameAlgo.startGame();
        int ind=0;
        while (gameAlgo.isRunning()){
            algorithm();
            if(ind%2==0) {gui.repaint();}
            ind++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void algorithm() {
        try {
            gameAlgo.getPokemons().update();
           // gameAlgo.getGame().move();
            gameAlgo.sendAgentsToPokemons();
            gameAlgo.getAgents().update();
            gameAlgo.moveAgents();
            gameAlgo.getInfo().update();
            gameAlgo.updateHandled();
            Thread.sleep(100);
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
//    public static void main(String[] args) throws JSONException {
//
//        game_service game = Game_Server_Ex2.getServer(11);
//        Information i = new Information(game);
//        DWGraph_Algo algo = new DWGraph_Algo();
//        directed_weighted_graph graph = algo.readFromJson(game.getGraph());
//        algo.init(graph);
//        Agents a = new Agents(game, i);
//        Pokemons p = new Pokemons(game, i);
//        GameAlgo gameAlgo = new GameAlgo(game, i, algo, p, a);
//
//        Thread gameThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (gameAlgo.getGame().isRunning()) {
//                        gameAlgo.getPokemons().update();
//                        //gameAlgo.getGame().move();
//                        gameAlgo.sendAgentsToPokemons();
//                        gameAlgo.getAgents().update();
//                        gameAlgo.moveAgents();
//                        gameAlgo.updateHandled();
//
//                        Thread.sleep(100);
//                        System.out.println(game.timeToEnd() / 1000);
//                        System.out.println(game.toString());
//                        System.out.println(game.getAgents());
//                    }
//                } catch (JSONException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        gameAlgo.startGame();
//        System.out.println(game.timeToEnd() / 1000);
//        gameThread.start();
//    }