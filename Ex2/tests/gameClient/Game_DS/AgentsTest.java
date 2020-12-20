package gameClient.Game_DS;

import Server.Game_Server_Ex2;
import api.game_service;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import api.*;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class AgentsTest {
    @Test
    void TestAgents() throws JSONException {
        game_service game = Game_Server_Ex2.getServer(11);
        Information info=new Information(game);
        for(int i=0;i<info.getAgents();i++)game.addAgent(i);
        Agents agents=new Agents(game,info);
        agents.update();
        Iterator<Agent> itr=agents.iterator();
        while (itr.hasNext())
            System.out.println(itr.next().toString());
        assertEquals(3,agents.size());
        DWGraph_Algo algo=new DWGraph_Algo();

        assertEquals("data/A2",info.getGraph());

    }
}