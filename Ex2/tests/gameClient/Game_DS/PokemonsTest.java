package gameClient.Game_DS;

import Server.Game_Server_Ex2;
import api.game_service;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class PokemonsTest {

    @Test
    void TestPokemons() throws JSONException {
        game_service game = Game_Server_Ex2.getServer(11);
        Information info=new Information(game);
        for(int i=0;i<info.getPokemons();i++)game.addAgent(i);
        Pokemons pokemons=new Pokemons(game,info);
        pokemons.update();
        Iterator<Pokemon> itr=pokemons.iterator();
        while (itr.hasNext())
            System.out.println(itr.next().toString());
        assertEquals(6,pokemons.size());
    }
}