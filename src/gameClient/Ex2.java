package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import api.directed_weighted_graph;
import api.game_service;
import gameClient.Game_DS.*;
import org.json.JSONException;

/**
 * This is the main class, from here we run the project's code.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */


public class Ex2 implements Runnable {
    private static GameGUI gui;
    private static GameAlgo gameAlgo;
    public static Integer level;
    public static Integer id;
    private static Thread gex2;
    public static void main(String[] args) {
        Thread ex2 = new Thread(new Ex2());
        try {
            if (level == null && id == null) {
                level = Integer.parseInt(args[1]);
                id = Integer.parseInt(args[0]);
                gui = new GameGUI();
            }
        } catch (Exception e) {
            gui = new GameGUI();
        }
         gex2=new Thread(new Runnable() {
            @Override
            public void run() {
                while (gameAlgo.isRunning())
                        gui.repaint();
            }
        });
        ex2.start();

    }
    @Override
    public void run() {
        init();
        gui.update(gameAlgo);
        gameAlgo.startGame();
        gex2.start();
        while (gameAlgo.isRunning()) {algorithm();}
        System.out.println(gameAlgo.getGame().toString());
    }

    private static void algorithm() {
        try {
            gameAlgo.getGame().move();
            gameAlgo.sendAgentsToPokemons();
            gameAlgo.moveAgents();
            gameAlgo.getInfo().update();
            Thread.sleep(gameAlgo.averageTime());
        } catch (InterruptedException | JSONException e) {e.printStackTrace();}
    }
    
    public static void init() {
        game_service game = Game_Server_Ex2.getServer(level);
        game.login(id);
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
