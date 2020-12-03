package gameClient.Game_DS;

import Server.Game_Server_Ex2;
import api.game_service;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class PokemonsTest {

    @Test
    void update() throws JSONException {
        game_service game = Game_Server_Ex2.getServer(11);
        Pokemons P= new Pokemons(game);
        Iterator g=P.iterator();
        while(g.hasNext()){
            System.out.println(g.next().toString());
        }
     int y=0;

    }

    @Test
    void size() {
    }
}