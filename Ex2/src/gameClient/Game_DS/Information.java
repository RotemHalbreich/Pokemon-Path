package gameClient.Game_DS;

import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

public class Information {
    private int pokemons;
    private int agents;
    private String graph;
    private game_service game;
    private int moves;
    private int grade;
    private int gameLevel;

    public Information(game_service game) throws JSONException {
        this.game = game;
        update();
    }

    public void update() throws JSONException {
        JSONObject serverJSON = new JSONObject(game.toString()).getJSONObject("GameServer");
        setPokemons(serverJSON.getInt("pokemons"));
        setAgents(serverJSON.getInt("agents"));
        setGraph(serverJSON.getString("graph"));
        setMoves(serverJSON.getInt("moves"));
        setGameLevel(serverJSON.getInt("game_level"));
        setGrade(serverJSON.getInt("grade"));
    }
    public void setGameLevel(int level){this.gameLevel=level;}
    public int getGameLevel(){return this.gameLevel;}
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getPokemons() {
        return pokemons;
    }

    public void setPokemons(int pokemons) {
        this.pokemons = pokemons;
    }

    public int getAgents() {
        return agents;
    }

    public void setAgents(int agents) {
        this.agents = agents;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }


}
