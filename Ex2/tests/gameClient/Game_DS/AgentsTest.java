package gameClient.Game_DS;

import Server.Game_Server_Ex2;
import api.game_service;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class AgentsTest {

    @Test
    void update() throws JSONException {
        game_service game = Game_Server_Ex2.getServer(11); // you have [0,23] games
        Agents a=new Agents(game);
        game.addAgent(0);
        game.addAgent(0);
        game.addAgent(0);
        a.update();
        Iterator itr =a.iterator();
        while (itr.hasNext()){
            System.out.println(itr.next().toString());
        }
    }

    @Test
    void size() {
    }
}