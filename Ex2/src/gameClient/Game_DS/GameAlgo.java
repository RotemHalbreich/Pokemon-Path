package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONException;

import java.util.*;

public class GameAlgo extends Thread {
    private game_service game;
    private Information info;
    private dw_graph_algorithms algo;
    private Pokemons pokemons;
    private Agents agents;

    private List<Point3D> handlingPokemons = new ArrayList<>();
    private HashMap<Integer, ArrayList<node_data>> targets = new HashMap<>();
    private HashMap<Integer, Double> timer = new HashMap<>();

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

    public void sendAgentsToPokemons() {
        Iterator<Pokemon> itr = pokemons.iterator();
        while (itr.hasNext()) {
            Pokemon currPok = itr.next();
            if (!handlingPokemons.contains(currPok.getLocation())) {
                handlingPokemons.add(0, currPok.getLocation());
                if (!onWay(currPok)) findBestAgent(currPok);
            }
        }
        try {
            updateHandled();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void moveAgents() {
        Iterator<Agent> itr = agents.iterator();
        while (itr.hasNext()) {
            Agent a = itr.next();
            if (a.getDest() == -1) moveAgent(a);
        }
    }

    public directed_weighted_graph getGraph() {
        return algo.getGraph();
    }

    @Override
    public void run() {
        try {
            pokemons.update();
            agents.update();
            info.update();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        game.startGame();
    }

    public game_service getGame() {
        return game;
    }

    public Pokemons getPokemons() {
        return pokemons;
    }

    public Agents getAgents() {
        return agents;
    }

    public Information getInfo() {
        return info;
    }

    public synchronized boolean isRunning() {
        return game.isRunning();
    }

    public long averageTime() {
        Iterator<Agent> itr = agents.iterator();
        double ans = 1, w = 0, speed = 0, min = Double.MAX_VALUE;
        while (itr.hasNext()) {
            Agent a = itr.next();
            ArrayList<node_data> p = targets.get(a.getId());
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
        if (ans < 100) ans = (100 + min);
        return (long) ans;
    }

    ////////// Private Methods //////////
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

    private boolean onWay(Pokemon currPok) {
        for (int id : targets.keySet()) {
            if (hasAgentGoTO(targets.get(id), currPok.getEdge()))
                return true;
        }
        return false;
    }

    private boolean hasAgentGoTO(ArrayList<node_data> path, edge_data edge) {
        for (int i = 0; i < path.size() - 1; i++) {
            int s = path.get(i).getKey(),d = path.get(i + 1).getKey();
            if (s == edge.getSrc() && d == edge.getDest())
                return true;
        }
        return false;
    }

    private void findBestAgent(Pokemon currPok) {
        int src = currPok.getEdge().getSrc();
        //long optimalTime = game.timeToEnd() + 1000;
        double optimalTime = Double.MAX_VALUE;
        ArrayList<node_data> path = null, tempPath = null;
        int last = 0, agentId = 0;

        Iterator<Agent> itr = agents.iterator();
        while (itr.hasNext()) {
            Agent agent = itr.next();
            Double tempOptimalTime = timer.get(agent.getId());
            tempPath = targets.get(agent.getId());
            last = tempPath.get(tempPath.size() - 1).getKey();

            //double minTime = algo.shortestPathDist(last, src) / agent.getSpeed();
            double minTime = agents.DPS1(last, src) / agent.getSpeed();
            //       double minTime=agents.DPS(agent.getId(),last,src)/agent.getSpeed();
            if (minTime + tempOptimalTime < optimalTime) {
                optimalTime = minTime + tempOptimalTime;
                path = tempPath;
                agentId = agent.getId();
            }

        }
        last = path.get(path.size() - 1).getKey();
        List<node_data> shortesPath = algo.shortestPath(last, src);
        shortesPath.remove(0);
        path.addAll(shortesPath);
        path.add(algo.getGraph().getNode(currPok.getEdge().getDest()));

        timer.put(agentId, optimalTime);
        //handlingPokemons.add(0, currPok.getLocation());
    }

    private void updateHandled() throws JSONException {
        int handled = info.getPokemons() + 1;
        while (handlingPokemons.size() > handled)
            handlingPokemons.remove(handled);
    }

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

    private double eatPokemon(edge_data e) {
        double ans = -1, min = Double.MAX_VALUE;
        Iterator<Pokemon> itr = pokemons.iterator();
        while (itr.hasNext()) {
            Pokemon pok = itr.next();
            edge_data edge = pok.getEdge();
            if (edge!=null&&edge.equals(e)) {
                double temp = algo.getGraph().getNode(edge.getSrc()).getLocation().distance(pok.getLocation());
                if (temp < min) min = temp;
            }
        }
        if (min != Double.MAX_VALUE) ans = min;
        return ans;
    }

}
