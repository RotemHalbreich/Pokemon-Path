package gameClient.Game_DS;

import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents the game's information.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Information {
    private int pokemons;
    private int agents;
    private String graph;
    private game_service game;
    private int moves;
    private int grade;
    private int gameLevel;

    /**
     * Constructor:
     *
     * @param game
     * @throws JSONException
     */
    public Information(game_service game) throws JSONException {
        this.game = game;
        update();
    }

    /**
     * Updates the game's information.
     *
     * @throws JSONException
     */
    public void update() throws JSONException {
        JSONObject serverJSON = new JSONObject(game.toString()).getJSONObject("GameServer");
        setPokemons(serverJSON.getInt("pokemons"));
        setAgents(serverJSON.getInt("agents"));
        setGraph(serverJSON.getString("graph"));
        setMoves(serverJSON.getInt("moves"));
        setGameLevel(serverJSON.getInt("game_level"));
        setGrade(serverJSON.getInt("grade"));
    }

    /**
     * Sets the game's level.
     *
     * @param level
     */
    public void setGameLevel(int level) {
        this.gameLevel = level;
    }

    /**
     * Gets the game's level.
     *
     * @return int
     */
    public int getGameLevel() {
        return this.gameLevel;
    }

    /**
     * Gets the grade.
     *
     * @return int
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Sets the grade.
     *
     * @param grade
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Gets the moves.
     *
     * @return int
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Sets the moves.
     *
     * @param moves
     */
    public void setMoves(int moves) {
        this.moves = moves;
    }

    /**
     * Gets the Pokemons.
     *
     * @return int
     */
    public int getPokemons() {
        return pokemons;
    }

    /**
     * Sets the Pokemons.
     *
     * @param pokemons
     */
    public void setPokemons(int pokemons) {
        this.pokemons = pokemons;
    }

    /**
     * Gets the Agents.
     *
     * @return agents.
     */
    public int getAgents() {
        return agents;
    }

    /**
     * Sets the Agents.
     *
     * @param agents
     */
    public void setAgents(int agents) {
        this.agents = agents;
    }

    /**
     * Gets the graph as a String.
     *
     * @return String
     */
    public String getGraph() {
        return graph;
    }

    /**
     * Sets the graph as a String.
     *
     * @param graph
     */
    public void setGraph(String graph) {
        this.graph = graph;
    }
}
