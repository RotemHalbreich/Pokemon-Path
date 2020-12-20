package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONException;

import java.util.*;

/**
 * Represents the game's algorithms.
 * game - is the game server.
 * info - the game's information.
 * algo - graph algorithm.
 * pokemons - the game's Pokemons.
 * agents - the game's Agents.
 * handlingPokemons - an ArrayList representing the Pokemons who need to be caught.
 * targets - includes the path for of any agent to his Pokemon.
 * timer - includes the estimated time on which every Agent gets to his targeted Pokemon.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class GameAlgo {
    private game_service game;
    private Information info;
    private dw_graph_algorithms algo;
    private Pokemons pokemons;
    private Agents agents;
    private List<Point3D> handlingPokemons = new ArrayList<>();
    private HashMap<Integer, ArrayList<node_data>> targets = new HashMap<>();
    private HashMap<Integer, Double> timer = new HashMap<>();

    /**
     * Constructor:
     *
     * @param game
     * @param i
     * @param algo
     * @param p
     * @param a
     * @throws JSONException
     */
    public GameAlgo(game_service game, Information i, dw_graph_algorithms algo, Pokemons p, Agents a) throws JSONException {
        this.game = game;
        this.info = i;
        this.algo = algo;
        this.pokemons = p;
        this.agents = a;
        insertFirstTime();
        agents.update();
        insertTargetsAndTime();
    }
    /**
     * Sends the agents to the pokemons on the graph,
     * updates the Pokemon list and handlingPokemons list.
     */
    public void sendAgentsToPokemons() {
        try {
            pokemons.update();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<Pokemon> itr = pokemons.iterator();
        while (itr.hasNext()) {
            Pokemon currPok = itr.next();
            if (!handlingPokemons.contains(currPok.getLocation())) {
                if (!onWay(currPok)) findBestAgent(currPok);
                handlingPokemons.add(0, currPok.getLocation());
            }
        }
        try {
            updateHandled();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves the agents on the graph and updates the agents.
     */
    public void moveAgents() {
        try {
            agents.update();
        } catch (JSONException e) {
            e.printStackTrace();
        }
       for(Integer id:timer.keySet()){
           Agent a=agents.getAgent(id);
           if (a.getDest() == -1) moveAgent(a);
       }
    }

    /**
     * Gets a directed weighted graph.
     *
     * @return directed_weighted_graph
     */
    public directed_weighted_graph getGraph() {
        return algo.getGraph();
    }

    /**
     * Starts the game.
     */
    public void startGame() {
        game.startGame();
    }

    /**
     * Returns the game server.
     *
     * @return game_service
     */
    public game_service getGame() {
        return game;
    }

    /**
     * Returns the game's Pokemons.
     *
     * @return Pokemons
     */
    public Pokemons getPokemons() {
        return pokemons;
    }

    /**
     * Returns the game's agents.
     *
     * @return Agents
     */
    public Agents getAgents() {
        return agents;
    }

    /**
     * Returns the game's information.
     *
     * @return Information
     */
    public Information getInfo() {
        return info;
    }

    /**
     * Checks if the game is currently running.
     *
     * @return boolean
     */
    public synchronized boolean isRunning() {
        return game.isRunning();
    }

    /**
     * Calculates the average time the game is "allowed" to sleep.
     *
     * @return long
     */
    public  long averageTime() {
        double ans = 1, w = 0, speed = 0, min = Double.MAX_VALUE;
        for (Integer id:timer.keySet()){
            Agent a = agents.getAgent(id);
            ArrayList<node_data> p = targets.get(id);
            if (p.size() > 1) {
                edge_data edge = algo.getGraph().getEdge(p.get(0).getKey(), p.get(1).getKey());
                double eat = eatPokemon(edge);
                speed = a.getSpeed();
                w = edge.getWeight();
                if (eat != -1) min = eat / speed;
                if ((w / speed) < min) min = w / speed;
            }
        }
        if (min == Double.MAX_VALUE) ans = 100;
        else ans = min * 100;
        if (ans < 70) ans = (100 + min);
        return (long) ans;
    }

    ////////// Private Methods //////////

    /**
     * Initializes for the first time the targets and the timer in the agents' location.
     */
    private void insertTargetsAndTime() {
        Iterator<Agent> itr = agents.iterator();
        while (itr.hasNext()) {
            ArrayList<node_data> t = new ArrayList<>();
            Agent a = itr.next();
            t.add(algo.getGraph().getNode(a.getSrc()));
            targets.put(a.getId(), t);
            timer.put(a.getId(), 0.0);
        }
    }

    /**
     * Initializes for the first time the agents in close range to the Pokemons.
     * If there are more agents than Pokemons, chooses a random location to the agent.
     *
     * @throws JSONException
     */
    private void insertFirstTime() throws JSONException {
        Iterator<Pokemon> itr = pokemons.iterator();
        for (int i = 0; i < agents.size(); i++) {
            if (itr.hasNext()) {
                int src = itr.next().getEdge().getSrc();
                game.addAgent(src);
            } else {
                int r = algo.getGraph().nodeSize();
                game.addAgent((int) (Math.random() * r));
            }
        }
    }

    /**
     * Checks if an agent is on the way to catch a pokemon.
     *
     * @param currPok
     * @return boolean
     */
    private boolean onWay(Pokemon currPok) {
        for (int id : targets.keySet()) {
            if (hasAgentGoTO(targets.get(id), currPok.getEdge()))
                return true;
        }
        return false;
    }

    /**
     * Checks if the agent and the Pokemon are on the same edge.
     *
     * @param path
     * @param edge
     * @return boolean
     */
    private boolean hasAgentGoTO(ArrayList<node_data> path, edge_data edge) {
        for (int i = 0; i < path.size() - 1; i++) {
            int s = path.get(i).getKey(), d = path.get(i + 1).getKey();
            if (s == edge.getSrc() && d == edge.getDest())
                return true;
        }
        return false;
    }

    /**
     * Help function: finds the most efficient agent for catching the current Pokemon based on the
     * speed arrival to it and finds the shortest path between them.
     * Updates the target and timer.
     *
     * @param currPok
     */
    private void findBestAgent(Pokemon currPok) {
        int src = currPok.getEdge().getSrc();
        double optimalTime = game.timeToEnd() + 1000;
        ArrayList<node_data> path = null, tempPath = null;
        int last = 0, agentId = 0;
        for (Integer id:timer.keySet()){
            Agent agent = agents.getAgent(id);
            Double tempOptimalTime = timer.get(id);
            tempPath = targets.get(id);
            last = tempPath.get(tempPath.size() - 1).getKey();
            double minTime = agents.DPS(last, src) / agent.getSpeed();
            if (minTime + tempOptimalTime < optimalTime) {
                optimalTime = minTime + tempOptimalTime;
                path = tempPath;
                agentId = id;
            }
        }
        if (path == null) return;
        last = path.get(path.size() - 1).getKey();
        List<node_data> shortesPath = algo.shortestPath(last, src);
        shortesPath.remove(0);
        path.addAll(shortesPath);
        path.add(algo.getGraph().getNode(currPok.getEdge().getDest()));
        timer.put(agentId, optimalTime);
    }

    /**
     * Updates the list of handlingPokemons.
     *
     * @throws JSONException
     */
    private void updateHandled() throws JSONException {
        int handled = info.getPokemons() + 1;
        while (handlingPokemons.size() > handled)
            handlingPokemons.remove(handled);
    }

    /**
     * Moves the agent to the next vertex and updated the timer.
     *
     * @param a
     */
    private void moveAgent(Agent a) {
        ArrayList<node_data> path = targets.get(a.getId());
        if (path.size() > 1) {
            int src = path.get(0).getKey(), dest = path.get(1).getKey();
            game.chooseNextEdge(a.getId(), path.get(1).getKey());
            edge_data from = algo.getGraph().getEdge(src, dest);
            double fromTime = (from.getWeight() / a.getSpeed());
            if (timer.get(a.getId()) - fromTime < 0) {
                timer.put(a.getId(), 0.0);
            } else timer.put(a.getId(), timer.get(a.getId()) - fromTime);
            path.remove(0);
        } else timer.put(a.getId(), 0.0);
    }

    /**
     * Checks if there's any Pokemon on this edge.
     * if exists, returns the distance from src to this Pokemon.
     *
     * @param e
     * @return double
     */
    private double eatPokemon(edge_data e) {
        double ans = -1, min = Double.MAX_VALUE;
        Iterator<Pokemon> itr = pokemons.iterator();
        while (itr.hasNext()) {
            Pokemon pok = itr.next();
            edge_data edge = pok.getEdge();
            if (edge != null && edge.equals(e)) {
                double temp = algo.getGraph().getNode(edge.getSrc()).getLocation().distance(pok.getLocation());
                if (temp < min) min = temp;
            }
        }
        if (min != Double.MAX_VALUE) ans = min;
        return ans;
    }
}
