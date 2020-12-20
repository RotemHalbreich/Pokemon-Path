package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import api.directed_weighted_graph;
import api.game_service;
import gameClient.Game_DS.*;
import org.json.JSONException;

/**
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Ex2 implements Runnable {
    private static GameGUI gui;
    private static GameAlgo gameAlgo;
    public static Integer level;
    public  static Integer id;

    public static void main(String[] args) {
        Thread ex2 = new Thread(new Ex2());
        try{
            if(level==null&&id==null) {
                level = Integer.parseInt(args[1]);
                id = Integer.parseInt(args[0]);
                gui = new GameGUI();
            }
        }catch (Exception e){gui = new GameGUI();}
        ex2.start();
    }

    /**
     *
     */
    @Override
    public void run() {

        init();
        gui.update(gameAlgo);
        gameAlgo.startGame();
        int ind=0;
      // gameAlgo.start();
        while (gameAlgo.isRunning()){
            algorithm();
            if(ind%1==0) {gui.repaint();}
            ind++;
        }
        gui.repaint();
        System.out.println(gameAlgo.getGame().toString());

    }
//
    /**
     *
     */
    private static void algorithm() {
        try {
            gameAlgo.getGame().move();
          //  gameAlgo.getPokemons().update();
            //ameAlgo.getGame().move();
            gameAlgo.sendAgentsToPokemons();
          //  gameAlgo.getAgents().update();
            gameAlgo.moveAgents();
             gameAlgo.getInfo().update();
          //  gameAlgo.updateHandled();

            Thread.sleep(gameAlgo.averageTime());
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public static void init(){

        game_service game = Game_Server_Ex2.getServer(level);
       // game.login(id);
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
