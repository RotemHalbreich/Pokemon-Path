package gameClient.Game_DS;

import api.*;

import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.parseDouble;


public class GameAlgo2 {
    private game_service game;
    private Information info;
    private DWGraph_Algo algo;
    private Pokemons pokemons;
    private Agents agents;
    private HashMap<Integer, LinkedList<node_data>> path = new HashMap<>();
    private LinkedList<Pokemon> needHandling = new LinkedList<>();
    public GameAlgo2(game_service game)  {
        this.game = game;
        try {
            this.info = new Information(game);
            algo = new DWGraph_Algo();
            directed_weighted_graph graph=algo.readFromJson(game.getGraph());
            algo.init(graph);

            this.pokemons = new Pokemons(this.game, info);
            this.agents = new Agents(this.game, info);

            firstTime();
            agents.update();
            matchPokemonToAgent();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void firstTime() {
        Iterator<Pokemon> pok = pokemons.iterator();
        for (int i = 0; i < info.getAgents(); i++) {
            if (pok.hasNext()) {
                Pokemon p = pok.next();
                int src = p.getEdge().getSrc();
                game.addAgent(src);

            } else {
                int r = algo.getGraph().nodeSize();
                game.addAgent((int) (Math.random() * r));
            }
        }
    }
    private void matchPokemonToAgent (){
            Iterator<Agent> itr = agents.iterator();
            while (itr.hasNext()) {
                Agent agent = itr.next();
                Iterator<Pokemon> pok = pokemons.iterator();
                while (pok.hasNext()) {
                    Pokemon pokemon = pok.next();
                    // we need strong that
                    if (pokemon.getEdge().getSrc() == agent.getSrc()){
                        agent.setTarget(pokemon);
                        LinkedList<node_data> p=new LinkedList<>();
                      node_data s=algo.getGraph().getNode(pokemon.getEdge().getSrc()),d=algo.getGraph().getNode(pokemon.getEdge().getDest());
                        p.add(algo.getGraph().getNode(pokemon.getEdge().getSrc()));
                        p.add(algo.getGraph().getNode(pokemon.getEdge().getDest()));
                        path.put(agent.getId(),p);
                    }

                }
            }
        }

    public void moveAgents(){
        Iterator<Agent> itr= getIteratorAgents();

        while (itr.hasNext()){
            System.out.println("move agent");
            Agent agent= itr.next();
            if(agent.getDest()==-1)
                nextNode(agent.getId());
        }
    }

    private void nextNode(Integer id) {
       if(path.get(id).size()>1){
           int dest= path.get(id).get(1).getKey();
           game.chooseNextEdge(id,dest);
           path.get(id).remove(0);
       }
       else {
           // pokemonToNotBusy(id);
           System.out.println("next node");

       }
    }
    public boolean isRunning(){
        return game.isRunning();
    }
    private void pokemonToNotBusy(Integer id) {
        double max= MAX_VALUE;
        edge_data e=null;
        Pokemon p=null;
        for(Pokemon pok:needHandling){
            double op=algo.shortestPathDist(
                    path.get(id).get(0).getKey(),
                    pok.getEdge().getSrc()
            );
            if(op<max){
                e= pok.getEdge();
                max=op;
                p=pok;
            }
        }
        LinkedList<node_data> currPath= (LinkedList<node_data>)
                algo.shortestPath(
                    path.get(id).get(0).getKey(),
                    e.getSrc()
        );
        path.put(id,currPath);
        path.get(id).addLast(algo.getGraph().getNode(e.getDest()));
        path.get(id).remove(0);
        agents.getAgent(id).setTarget(p);
        needHandling.remove(p);
    }
    public void updateHandling(){
        Iterator<Pokemon>pok=pokemons.iterator();
        while (pok.hasNext()){
            Pokemon p=pok.next();
            if(!needHandling.contains(p)){
                if(!onWay(p)) findBestAgent(p);
                else needHandling.addFirst(p);
            }
        }
    }

    private void findBestAgent(Pokemon p) {
            Integer src =p.getEdge().getSrc(),
            agent=null;
        long optimalTime=game.timeToEnd()+1000;
        double minTime=MAX_VALUE;
        for(Integer id:path.keySet()){
            double timeToEnd=algo.shortestPathDist(
                    path.get(id).get(0).getKey(),
                    path.get(id).get(path.get(id).size()-1).getKey()
            ),
             timeToPokemon= algo.shortestPathDist(
                     path.get(id).get(path.get(id).size()-1).getKey(),
                     src
             );
            if(minTime>timeToEnd+timeToPokemon){
                agent=id;
                minTime=timeToEnd+timeToPokemon;
            }
        }
        LinkedList<node_data> curPath= (LinkedList<node_data>)
                algo.shortestPath(
                path.get(agent).get(
                        path.get(agent).size()-1).getKey(),src
        );
        for(node_data n: curPath)
            path.get(agent).addLast(n);
       path.get(agent).addLast(
               algo.getGraph().getNode(p.getEdge().getDest())
       );
       agents.getAgent(agent).setTarget(p);
       needHandling.addFirst(p);
    }

    private boolean onWay(Pokemon p) {
       Iterator<Agent> agent= agents.iterator();
       while (agent.hasNext()){
           if(agent.next().target.contains(p))
               return true;
       }

       for(Integer id: path.keySet()){
         if(onEdge(id,path.get(id),p))
             return true;
       }
       return false;
    }

    private boolean onEdge(int id,LinkedList<node_data> curPath, Pokemon p) {
        if(curPath.size()>1) {
            for (int i = 0; i < curPath.size() - 1; i++) {
                int s = curPath.get(i).getKey(),
                        d = curPath.get(i + 1).getKey();
                if (s == p.getEdge().getSrc() && d == p.getEdge().getDest()) {
                    agents.getAgent(id).setTarget(p);
                    return true;
                }
            }
        }
        return false;
    }
    public void refreshHandling(){
        int p=info.getPokemons()+1;
        while (needHandling.size()>p)
            needHandling.removeLast();
    }
    public  void updatePokemons(){
        try {
            pokemons.update();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void updateInfo(){
        try {
            info.update();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void updateAgents(){
        try {
            agents.update();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public directed_weighted_graph getGraph() {
        return algo.getGraph();
    }

    public game_service getGame() {
        return game;
    }

    public Agents getAgents() {
        return agents;
    }

    public Iterator<Agent> getIteratorAgents(){
        return agents.iterator();
    }

    public Pokemons getPokemons() {
        return pokemons;
    }
    public Iterator<Pokemon> getIteratorPokemons(){
        return pokemons.iterator();
    }

    /////////// Private Classes///////////
    public class Agents {
        private final String isEmpty = "{\"Agents\":[";
        private game_service game;
        private Information info;
        private HashMap<Integer, Agent> agents;
        private double[][] dps;

        public Agents(game_service game, Information i) throws JSONException {
            this.game = game;
            this.info = i;
            update();
            insertDPS();

        }


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

        public int size() throws JSONException {
            return info.getAgents();

        }

        public Iterator<Agent> iterator() {
            return this.agents.values().iterator();
        }

        public synchronized Agent getAgent(int key) {
            return agents.get(key);
        }

        public long DPS(int key, int i, int j) {
            Agent a = getAgent(key);
            return (long) (dps[i][j] / a.getSpeed());
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Integer i : agents.keySet()) {
                sb.append(agents.get(i).toString());
            }
            return "" + sb;
        }

        /////////// Private Methods //////////
        private void insertDPS() {
            directed_weighted_graph g = new DWGraph_Algo().readFromJson(game.getGraph());
            int s = findMaxId(g);
            if (s == -1 || s == 0) return;
            dps = new double[s][s];
            insertDefaultValue(g, s);
            insertActualValue(g);
            minDPS();
        }

        private int findMaxId(directed_weighted_graph g) {
            int m = -1;
            for (node_data n : g.getV()) {
                if (m < n.getKey())
                    m = n.getKey();
            }
            return m + 1;
        }

        private void insertDefaultValue(directed_weighted_graph g, int s) {
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < s; j++) {
                    if (i == j && g.getNode(i) != null)
                        dps[i][j] = 0;
                    else dps[i][j] = MAX_VALUE;
                }
            }
        }

        private void insertActualValue(directed_weighted_graph g) {
            for (node_data n : g.getV()) {
                for (edge_data e : g.getE(n.getKey())) {
                    dps[e.getSrc()][e.getDest()] = e.getWeight();
                }
            }
        }

        private void minDPS() {
            for (int k = 0; k < dps.length; k++) {
                for (int i = 0; i < dps.length; i++) {
                    for (int j = 0; j < dps.length; j++) {
                        if (dps[i][j] > dps[i][k] + dps[k][j])
                            dps[i][j] = dps[i][k] + dps[k][j];
                    }
                }
            }
        }

    }

    /////////// Private Class///////////
    public class Agent {
        private Integer id;
        private double value;
        private int src;
        private int dest;
        private double speed;
        private Point3D pos;
        private edge_data edge;
        private LinkedList<Pokemon> target = new LinkedList<>();

        public Agent(JSONObject json) throws JSONException {
            update(json);
        }

        public void update(JSONObject json) throws JSONException {
            id = json.getInt("id");
            src = json.getInt("src");
            dest = json.getInt("dest");
            value = json.getDouble("value");
            speed = json.getDouble("speed");
            double[] c = simplifyLocation(json.getString("pos"));
            pos = new Point3D(c[0], c[1], c[2]);
            //edge->we need think
            //node->we need think
        }

        private double[] simplifyLocation(String s) {
            double[] ans = new double[3];
            String[] as = s.split(",");
            ans[0] = parseDouble(as[0]);
            ans[1] = parseDouble(as[1]);
            ans[2] = parseDouble(as[2]);
            return ans;
        }

        public Integer getId() {
            return id;
        }


        public double getValue() {
            return value;
        }

        public int getSrc() {
            return src;
        }

        public int getDest() {
            return dest;
        }

        public double getSpeed() {
            return speed;
        }

        public Point3D getPos() {
            return pos;
        }

        public edge_data getEdge() {
            return edge;
        }

        public boolean isMoving() {
            return dest != -1;
        }

        public void setTarget(Pokemon p) {
            target.addFirst(p);
        }

        public LinkedList<Pokemon> getTarget() {
            return target;
        }

        public String toString() {
            return "Agent:{"
                    + "id:" + getId()
                    + ",src:" + getSrc()
                    + ",dest:" + getDest()
                    + ",pos:" + getPos().toString()
                    + "}";
        }
    }

    public class Pokemons {
        private game_service game;
        private Information info;
        private Pokemon[] pokemons;

        public Pokemons(game_service game, Information i) throws JSONException {
            this.game = game;
            this.info = i;
            update();
        }

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

        public int size() throws JSONException {
            return info.getPokemons();
        }

        public Iterator<Pokemon> iterator() {
            return Arrays.stream(pokemons).iterator();
        }

        public String toString() {
            return Arrays.toString(pokemons);
        }

    }


    public class Pokemon implements Comparable<Pokemon> {
        private game_service game;
        private double value;
        private int type;
        private Point3D pos;
        private edge_data edge;
        private final double EPS = 0.0000000000001;


        public Pokemon(JSONObject json, game_service game) throws JSONException {
            this.game = game;
            update(json);
        }

        public void update(JSONObject json) throws JSONException {
            value = json.getDouble("value");
            type = json.getInt("type");
            double[] c = simplifyPos(json.getString("pos"));
            pos = new Point3D(c[0], c[1], c[2]);
            try {
                setEdge();
            } catch (Exception e) {
                ;
            }
        }

        public double[] simplifyPos(String s) {
            double[] ans = new double[3];
            String[] as = s.split(",");
            ans[0] = parseDouble(as[0]);
            ans[1] = parseDouble(as[1]);
            ans[2] = parseDouble(as[2]);
            return ans;
        }

        public double getValue() {
            return value;
        }

        public int getType() {
            return type;
        }

        public Point3D getLocation() {
            return pos;
        }

        public void setEdge() {
            edge = null;
            directed_weighted_graph graph = new DWGraph_Algo().readFromJson(game.getGraph());
            for (node_data n : graph.getV()) {
                for (edge_data e : graph.getE(n.getKey())) {
                    if (isNotValidEdge(e)) continue;
                    Point3D src = (Point3D) graph.getNode(e.getSrc()).getLocation();
                    Point3D dest = (Point3D) graph.getNode(e.getDest()).getLocation();
                    double d = src.distance(dest) - src.distance(getLocation()) - getLocation().distance(dest);
                    if (Math.abs(d) < EPS)
                        edge = e;
                }
            }
        }


        public edge_data getEdge() {
            return edge;
        }


        public String toString() {
            return "Pokemon:" +
                    "{ "
                    + "val:" + getValue()
                    + ",type:" + getType()
                    + ",pos:" + getLocation().toString()
                    + ",edge:" + getEdge().toString()
                    + "}";
        }

        @Override
        public int compareTo(@NotNull Pokemon o) {
            Double v = getValue();
            return -v.compareTo(o.getValue());
        }

        ///////// Private Methods /////////
        private boolean isNotValidEdge(edge_data e) {
            return (type > 0) != (e.getSrc() < e.getDest());

        }



    }
    private class Information {
        private int pokemons;
        private int agents;
        private String graph;
        private game_service game;

        public Information(game_service game) throws JSONException {
            this.game=game;
            update();
        }
        public void update() throws JSONException {
            JSONObject serverJSON = new JSONObject(game.toString()).getJSONObject("GameServer");
            setPokemons(serverJSON.getInt("pokemons"));
            setAgents(serverJSON.getInt("agents"));
            setGraph(serverJSON.getString("graph"));
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

}




