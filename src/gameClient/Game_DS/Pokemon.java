package gameClient.Game_DS;

import api.*;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

/**
 * Represents the Pokemons in the Game.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Pokemon implements Comparable<Pokemon> {
    private game_service game;
    private double value;
    private int type;
    private Point3D pos;
    private edge_data edge;
    private final double EPS = 0.0000000000001;

    /**
     * Constructor:
     *
     * @param json
     * @param game
     * @throws JSONException
     */
    public Pokemon(JSONObject json, game_service game) throws JSONException {
        this.game = game;
        update(json);
    }

    /**
     * Updates the game's Pokemon.
     *
     * @param json
     * @throws JSONException
     */
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

    /**
     * Simplifies the position of the Pokemon.
     *
     * @param s
     * @return double[]
     */
    public double[] simplifyPos(String s) {
        double[] ans = new double[3];
        String[] as = s.split(",");
        ans[0] = parseDouble(as[0]);
        ans[1] = parseDouble(as[1]);
        ans[2] = parseDouble(as[2]);
        return ans;
    }

    /**
     * Gets the Pokemon's value.
     *
     * @return double
     */
    public double getValue() {
        return value;
    }

    /**
     * Gets the Pokemon's type.
     *
     * @return int
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the Pokemon's location.
     *
     * @return Point3D
     */
    public Point3D getLocation() {
        return pos;
    }

    /**
     * Adds an edge on which the Pokemon is located on.
     */
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

    /**
     * Gets the edge on which the Pokemon is located on.
     *
     * @return edge_data
     */
    public edge_data getEdge() {
        return edge;
    }

    /**
     * Returns the Pokemon as a String.
     *
     * @return String
     */
    public String toString() {
        return "Pokemon:" +
                "{ "
                + "val:" + getValue()
                + ",type:" + getType()
                + ",pos:" + getLocation().toString()
                + ",edge:" + getEdge().toString()
                + "}";
    }

    /**
     * Compares between two Pokemon values.
     *
     * @param o
     * @return int
     */
    @Override
    public int compareTo(@NotNull Pokemon o) {
        Double v = getValue();
        return -v.compareTo(o.value);
    }
    ///////// Private Methods /////////

    /**
     * Checks if the edge is valid.
     *
     * @param e
     * @return boolean
     */
    private boolean isNotValidEdge(edge_data e) {
        return (type > 0) != (e.getSrc() < e.getDest());

    }

}
