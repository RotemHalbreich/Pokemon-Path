package api;

import java.util.Objects;

/**
 * This class represents the set of operations applicable on a
 * node (vertex) in a directed weighted graph.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Node implements node_data {
    private int key;
    private geo_location location;
    private double weight;
    private String info;
    private int tag;

    /**
     * Constructor:
     *
      * @param key
     * @param location
     * @param weight
     * @param info
     * @param tag
     */
    public Node(int key, geo_location location, double weight, String info, int tag) {
        this.key = key;
        this.location = new Location(location);
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    /**
     * Constructor that gets a unique key and location.
     *
     * @param key
     * @param location
     */
    public Node(int key, geo_location location) {
        this(key, location, 0.0, "", 0);
    }

    /**
     * Copy Constructor:
     *
     * @param n
     */
    public Node(node_data n) {
        this(n.getKey(), n.getLocation(), n.getWeight(), n.getInfo(), n.getTag());
    }

    /**
     * Returns the unique key (ID) of this vertex.
     *
     * @return int
     */
    @Override
    public int getKey() {
        return key;
    }

    /**
     * Returns the location of this vertex,
     * if none, return null.
     *
     * @return geo_location
     */
    @Override
    public geo_location getLocation() {
        return location;
    }

    /**
     * Allows changing this vertex's location.
     *
     * @param p - the new location of this vertex.
     */
    @Override
    public void setLocation(geo_location p) {
        this.location = new Location(p);
    }

    /**
     * Returns the weight associated with this vertex.
     *
     * @return double
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this vertex's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Returns the information associated with this vertex.
     *
     * @return String
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the information associated with this vertex.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Returns the "tag" data of this vertex which can be used in
     * the algorithms of the graph.
     *
     * @return int
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * Allows setting the "tag" value for a vertex.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }


    /**
     * Returns the vertex as a String.
     *
     * @return String
     */

    @Override
    public String toString() {
        return "Node{" +
                "location=" + location +
                ", key=" + key +
                ", weight=" + weight +
                ", info='" + info + '\'' +
                ", tag=" + tag +
                '}';
    }

    /**
     * Checks if two vertices are equals.
     *
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof node_data)) return false;
        node_data n = (node_data) obj;
        return getKey() == n.getKey() && getLocation().equals(n.getLocation());
    }

    /**
     * hashCode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, location, weight, info, tag);
    }
}