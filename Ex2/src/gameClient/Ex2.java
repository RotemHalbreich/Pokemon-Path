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
    private static int level;

    public static void main(String[] args) {
        Thread ex2 = new Thread(new Ex2());
        try{
            level=Integer.parseInt(args[0]);
        }catch (Exception e){level=11;}
        ex2.start();

    }
    @Override
    public void run() {

        init();
        gui.update(gameAlgo);
        gameAlgo.startGame();
        int ind=0;
        gameAlgo.start();
        while (gameAlgo.isRunning()){
            algorithm();
            if(ind%1==0) {gui.repaint();}
            ind++;
        }
        System.out.println(gameAlgo.getGame().toString());

    }

    private static void algorithm() {
        try {
            gameAlgo.getGame().move();
            gameAlgo.getPokemons().update();
            //gameAlgo.getGame().move();
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
    public static void init(){
        game_service game = Game_Server_Ex2.getServer(level);
        //game.login()
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
    }
}
