package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.game_service;
import gameClient.Game_DS.*;
import org.json.JSONException;

public class Ex22 implements Runnable{
    private  GameGUI gui2;
    private  GameAlgo2 gameAlgo2;
    private static int level;

    public static void main(String[] args) {
        Thread ex2 = new Thread(new Ex22());
        try{
            level=Integer.parseInt(args[0]);
        }catch (Exception e){level=11;}
        ex2.start();
    }

    @Override
    public void run() {
        game_service game=Game_Server_Ex2.getServer(level);
        gameAlgo2=new GameAlgo2(game);
        gui2=new GameGUI();
        gui2.update(gameAlgo2);
        int i=0;
        gameAlgo2.getGame().startGame();
        while (gameAlgo2.isRunning()){
            gameAlgo2.getGame().move();
            gameAlgo2.updatePokemons();
            gameAlgo2.updateHandling();

            gameAlgo2.moveAgents();
            gameAlgo2.updateAgents();

            gameAlgo2.updateInfo();
            gameAlgo2.refreshHandling();
//            System.out.println(gameAlgo2.getGame().toString());
//            System.out.println(gameAlgo2.getGame().getAgents());
//            System.out.println(gameAlgo2.getGame().getPokemons());
        if(i%1==0) gui2.repaint();
            i++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
