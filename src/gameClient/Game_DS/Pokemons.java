package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.json.*;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents the list of Pokemons in the game.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Pokemons {
    private game_service game;
    private Information info;
    private Pokemon[] pokemons;

    /**
     * Constructor:
     *
     * @param game
     * @param i
     * @throws JSONException
     */
    public Pokemons(game_service game, Information i) throws JSONException {
        this.game = game;
        this.info = i;
        update();
    }

    /**
     * Updates the Pokemons of the game.
     *
     * @throws JSONException
     */
    public synchronized void update() throws JSONException {
        if (game == null || info == null) return;
        if (pokemons == null || pokemons.length != info.getPokemons())
            pokemons = new Pokemon[info.getPokemons()];

        JSONObject jsonPokemons = new JSONObject(game.getPokemons());
        JSONArray arrayPokemons = jsonPokemons.getJSONArray("Pokemons");

        for (int i = 0; i < size(); i++) {
            JSONObject jsonPokemon = arrayPokemons.getJSONObject(i).getJSONObject("Pokemon");
            if (pokemons[i] == null)
                pokemons[i] = new Pokemon(jsonPokemon, game);
            else
                pokemons[i].update(jsonPokemon);
        }
        Arrays.sort(pokemons);
    }

    /**
     * Returns the amount of Pokemons in the game.
     *
     * @return int
     * @throws JSONException
     */
    public int size() throws JSONException {
        return info.getPokemons();
    }

    /**
     * Returns an iterator of Pokemons.
     *
     * @return Iterator<Pokemon>
     */
    public synchronized Iterator<Pokemon> iterator() {
        return Arrays.stream(pokemons).iterator();
    }

    /**
     * Returns the list of Pokemons as a String.
     *
     * @returnv String
     */
    public String toString() {
        return Arrays.toString(pokemons);
    }


}
