package gameClient.Game_DS;

import api.*;
import org.json.*;
import gameClient.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;


import static java.lang.Double.parseDouble;

public class Pokemons {
    private game_service game;
    private Pokemon[] pokemons;

    public Pokemons(game_service game) throws JSONException {
        this.game = game;
        update();
    }

    public synchronized void update() throws JSONException {

        if (pokemons == null || pokemons.length != size())
            pokemons = new Pokemon[size()];

        JSONObject jsonPokemons = new JSONObject(game.getPokemons());
        JSONArray arrayPokemons = jsonPokemons.getJSONArray("Pokemons");

        for (int i = 0; i < size(); i++) {
            JSONObject jsonPokemon = arrayPokemons.getJSONObject(i).getJSONObject("Pokemon");
            if (pokemons[i] == null)
                pokemons[i] = new Pokemon(jsonPokemon,game);
            else
                pokemons[i].update(jsonPokemon);
        }
        Arrays.sort(pokemons);

    }

    public int size() throws JSONException {
        JSONObject g = new JSONObject(game.toString());
        JSONObject p = g.getJSONObject("GameServer");
        return p.getInt("pokemons");
    }

    public Iterator<Pokemon> iterator() {
        return Arrays.stream(pokemons).iterator();
    }

    public String toString() {
        return Arrays.toString(pokemons);
    }


}
