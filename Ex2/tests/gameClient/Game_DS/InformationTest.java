package gameClient.Game_DS;

import Server.Game_Server_Ex2;
import api.game_service;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InformationTest {
    @Test
    void TestInformation() throws JSONException {
        game_service game = Game_Server_Ex2.getServer(11);
        Information info=new Information(game);
        assertEquals(6,info.getPokemons());
        assertEquals(3,info.getAgents());
        assertEquals(info.getGraph(),info.getGraph());
    }
}