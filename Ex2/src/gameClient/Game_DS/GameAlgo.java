package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GameAlgo extends Thread {
    private game_service game;
    private Information info;
    private dw_graph_algorithms algo;
    private Pokemons pokemons;
    private Agents agents;
    private List<Point3D> handlingPokemons = new ArrayList<>();
    private HashMap<Integer, ArrayList<node_data>> targets = new HashMap<>();
    private HashMap<Integer, Long> timer = new HashMap<>();
    private String A = "";
    private String P = "";
    private String HP = "";
    private String T = "";


    public GameAlgo(game_service game, Information i, dw_graph_algorithms algo, Pokemons p, Agents a) throws JSONException {
        this.game = game;
        this.info = i;
        this.algo = algo;
        this.pokemons = p;
        this.agents = a;
        insertFirstTime();
        agents.update();

        //Experiment:
        A = game.getAgents();
        P = game.getPokemons();
        HP = handlingPokemons.toString();
        T = targets.toString();
        insertTargetsAndTime();
    }

    public void sendAgentsToPokemons() {
        game.move();
//        System.out.println(A);
//        System.out.println(P);
//        HP = handlingPokemons.toString();
//        System.out.println(HP);
//        T = targets.toString();
//        System.out.println(T);

//        if(!A.equals(game.getAgents())){
//            System.out.println("lllllllllllllllllllll");
//           //game.move();
//            A=game.getAgents();//table1
//        }else {
//            game.move(); //table1,
//            System.out.println("gggggggggggggggggggggg");
//        }

        Iterator<Pokemon> itr = pokemons.iterator();
        while (itr.hasNext()) {
            Pokemon currPok = itr.next();
            if (!handlingPokemons.contains(currPok.getLocation())) {
                if (onWay(currPok)) {
                    handlingPokemons.add(0, currPok.getLocation());
                } else {
                    findBestAgent(currPok);
                }
            }
        }
    }

    public void moveAgents() {

        Iterator<Agent> itr = agents.iterator();
        while (itr.hasNext()) {
            Agent a = itr.next();
            if (a.getDest() == -1)
                moveAgent(a);
        }
    }
    public directed_weighted_graph getGraph(){
        return algo.getGraph();
    }
    @Override
    public void run() {
        game.startGame();
        while (game.isRunning()) {

            try {
                pokemons.update();
                game.move();
                sendAgentsToPokemons();
                agents.update();
                moveAgents();
                updateHandled();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(game.toString());
            System.out.println(game.getAgents());
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
    public Information getInfo(){return info;}
    public synchronized boolean isRunning(){
        return game.isRunning();
    }

    ////////// Private Methods //////////
    private void insertTargetsAndTime() {
        Iterator<Agent> itr = agents.iterator();
        while (itr.hasNext()) {
            ArrayList<node_data> t = new ArrayList<>();
            Agent a = itr.next();
            t.add(algo.getGraph().getNode(a.getSrc()));
            targets.put(a.getId(), t);
            timer.put(a.getId(), 0L);
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
            int s = path.get(i).getKey(),
                    d = path.get(i + 1).getKey();
            if (s == edge.getSrc() && d == edge.getDest())
                return true;
        }
        return false;
    }

    private void findBestAgent(Pokemon currPok) {

        int src = currPok.getEdge().getSrc();
        long optimalTime = game.timeToEnd() + 1000;
        ArrayList<node_data> path = null, tempPath = null;
        int last = 0, agent = 0;
        for (int id : timer.keySet()) {
            long tempOptimalTime = timer.get(id);
            tempPath = targets.get(id);
            last = tempPath.get(tempPath.size() - 1).getKey();
            long minTime = agents.DPS(id, last, src);
            if (minTime + tempOptimalTime < optimalTime) {
                optimalTime = minTime + tempOptimalTime;
                path = tempPath;
                agent = id;
            }
        }
        /// update//
        last = path.get(path.size() - 1).getKey();

        List<node_data> shortesPath = algo.shortestPath(last, src);
        shortesPath.remove(0);
        path.addAll(shortesPath);
        path.add(algo.getGraph().getNode(currPok.getEdge().getDest()));
        timer.put(agent, optimalTime);
        handlingPokemons.add(0, currPok.getLocation());
    }

    public void updateHandled() throws JSONException {
        int handled = info.getPokemons() + 1;
        while (handlingPokemons.size() > handled)
            handlingPokemons.remove(handled);
    }

    private void moveAgent(Agent a) {
        ArrayList<node_data> path = targets.get(a.getId());
        if (path.size() > 1) {
            int src = path.get(0).getKey(), dest = path.get(1).getKey();
            game.chooseNextEdge(a.getId(), path.get(1).getKey());

            edge_data e = algo.getGraph().getEdge(src, dest);
            if (a.getPos().distance(pokemonFromEdge(e).getLocation()) < 0.001) {
//                System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhh");
//                System.out.println(A);
//                System.out.println(P);
//                HP = handlingPokemons.toString();
//                System.out.println(HP);
//                T = targets.toString();
//                System.out.println(T);

                game.move();
                A = game.getAgents();
            }

            edge_data from = algo.getGraph().getEdge(src, dest);
            long fromTime = (long) (from.getWeight() / a.getSpeed());
            timer.put(a.getId(), timer.get(a.getId()) - fromTime);
            path.remove(0);
        } else timer.put(a.getId(), 0L);
    }

    private void eatPokemon(Agent curr, int id, edge_data e) {
//        Pokemon p=pokemonFromEdge(e);
//        boolean cur=true;
//
//        while (cur){
//            System.out.println("Agent: "+curr.toString()+"\n");
//            System.out.println("pokemon: "+p.toString()+"\n");
//            System.out.println("edge: "+e.toString()+"\n");
//            game.move();
//
//            System.out.println(i +") outside");
//            if(curr.getPos().close2equals(p.getLocation())) {
//                System.out.println(j +") inside");
//                game.move();
//                cur=false;
//            }
//
//        }
    }

    private Pokemon pokemonFromEdge(edge_data e) {
        Iterator<Pokemon> itr = pokemons.iterator();
        Pokemon curr = null;
        while (itr.hasNext()) {
            curr = itr.next();
            if (curr.getEdge().equals(e))
                return curr;
        }
        return curr;
    }

    private boolean hasPokemonOnEdge(edge_data e) {
        Iterator<Pokemon> itr = pokemons.iterator();
        while (itr.hasNext()) {
            if (itr.next().getEdge().equals(e))
                return true;
        }
        return false;
    }

}
