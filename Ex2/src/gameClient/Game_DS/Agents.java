package gameClient.Game_DS;

import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Double.MAX_VALUE;

/**
 * Represents the list of the game's Agents.
 * <p>
 * game - the game server.
 * info - the game's information.
 * agents - the game's Agents.
 * dps - a matrix holds the shortest paths between a node to any other node.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Agents {

    private final String isEmpty = "{\"Agents\":[";
    private game_service game;
    private Information info;
    private HashMap<Integer, Agent> agents;

    private double[][] dps;

    /**
     * Constructor:
     *
     * @param game
     * @param i
     * @throws JSONException
     */
    public Agents(game_service game, Information i) throws JSONException {
        this.game = game;
        this.info = i;
        update();

        insertDPS();

    }

    /**
     * Updates the agents.
     *
     * @throws JSONException
     */
    public synchronized void update() throws JSONException {

        if (game == null || info == null) return;

        if (agents == null) agents = new HashMap<>();

        if (game.getAgents().equals(isEmpty)) return;

        JSONObject jsonAgents = new JSONObject(game.getAgents());
        JSONArray arrayAgents = jsonAgents.getJSONArray("Agents");

        for (int i = 0; i < size(); i++) {
            JSONObject jsonAgent = arrayAgents.getJSONObject(i).getJSONObject("Agent");
            int id = jsonAgent.getInt("id");

            if (agents.containsKey(id))
                agents.get(id).update(jsonAgent);
            else
                agents.put(id, new Agent(jsonAgent));
        }
    }

    /**
     * Returns the amount of agents in the game.
     *
     * @return int
     * @throws JSONException
     */
    public int size() throws JSONException {
        return info.getAgents();
    }

    /**
     * Returns an iterator of agents.
     *
     * @return Iterator<Agent>
     */
    public synchronized Iterator<Agent> iterator() {
        return agents.values().iterator();
    }

    /**
     * Returns an agent by a unique key.
     *
     * @param key
     * @return Agent
     */
    public synchronized Agent getAgent(int key) {
        return agents.get(key);
    }


    /**
     * Returns the minimal distance between src to dest.
     *
     * @param src
     * @param dest
     * @return double
     */
    public double DPS(int src, int dest) {
        double ans = dps[src][dest];
        return ans >= 0 ? ans : MAX_VALUE;
    }

    /**
     * Returns the agents as a String.
     *
     * @return String.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : agents.keySet()) {
            sb.append(agents.get(i).toString());
        }
        return "" + sb;
    }

    /////////// Private Methods //////////

    /**
     * Inserts the DPS in the minimum distances.
     */
    private void insertDPS() {
        directed_weighted_graph g = new DWGraph_Algo().readFromJson(game.getGraph());
        int s = findMaxId(g);
        if (s == -1 || s == 0) return;
        dps = new double[s][s];
        setMinPath(g);
    }

    /**
     * Calculates the shortest path distance between a vertex to any other vertex.
     *
     * @param g
     */
    private void setMinPath(directed_weighted_graph g) {
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g);
        for (node_data n : g.getV()) {
            for (node_data n1 : g.getV()) {
                dps[n.getKey()][n1.getKey()] = algo.shortestPathDist(n.getKey(), n1.getKey());
            }
        }
    }

    /**
     * Finds the maximum key in case the vertices' keys are note arranged serially.
     *
     * @param g
     * @return int
     */
    private int findMaxId(directed_weighted_graph g) {
        int m = -1;
        for (node_data n : g.getV()) {
            if (m < n.getKey())
                m = n.getKey();
        }
        return m + 1;
    }
}
