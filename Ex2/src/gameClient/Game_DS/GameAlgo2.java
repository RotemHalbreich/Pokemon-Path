package gameClient.Game_DS;

import api.*;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class GameAlgo2 {
    private game_service game;
    private dw_graph_algorithms algo;
    private Pokemons pokemons;
    private Information info;
    private Agents agents;
    private HashMap<Integer, LinkedList<node_data>> targets = new HashMap<>();
    private HashMap<Integer, Boolean> firstEnter = new HashMap<>();
    private HashMap<Integer, edge_data> firstEdge = new HashMap<>();

    public GameAlgo2(game_service game, Information i, dw_graph_algorithms algo, Pokemons p, Agents a) throws JSONException {
        this.game = game;
        this.info = i;
        this.algo = algo;
        this.pokemons = p;
        this.agents = a;

        insertFirst();

        agents.update();

    }

    ////////// Private Methods //////////
    private void insertFirst() throws JSONException {
        Iterator<Pokemon> pok = pokemons.iterator();
        for (int i = 0; i < info.getAgents(); i++) {
            if (pok.hasNext()) {
                Pokemon currPok = pok.next();
                game.addAgent(currPok.getEdge().getSrc());
                agents.update();
                Iterator<Agent> age = agents.iterator();
                while (age.hasNext()) {
                    Agent a = age.next();
                    if (!firstEnter.containsKey(a.getId())) {
                        firstEnter.put(a.getId(), true);
                        firstEdge.put(a.getId(), currPok.getEdge());
                    }
                }

            } else {
                int r = algo.getGraph().nodeSize();
                game.addAgent((int) (Math.random() * r));
            }
        }
    }

    public void moveAgents() {
        Iterator<Agent> ag = agents.iterator();
        while (ag.hasNext()) {
            Agent curr = ag.next();
            if (curr.getDest() == -1) moveAgent(curr);
        }
    }

    private void moveAgent(Agent curr) {
        LinkedList<node_data> tempT = targets.get(curr.getId());
        if (tempT.size() > 1) {
            int src = tempT.get(0).getKey(), dest = tempT.get(1).getKey();
            edge_data e = algo.getGraph().getEdge(src, dest);
            if (hasPokemonOnEdge(e)) {
                eatPokemon(curr, e);
            }

        } else if (firstEnter.get(curr.getId())) {
            ;
        } else {
            ;
        }// need path
    }

    private void eatPokemon(Agent curr, edge_data e) {
        Pokemon p = pokemonFromEdge(e);
        boolean con = true;
        while (con) {
            if (curr.getPos().close2equals(p.getLocation())) {
                game.move();
                con = false;
            }
        }
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




