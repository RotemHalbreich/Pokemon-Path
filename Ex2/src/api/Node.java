package api;

import java.util.HashMap;

/**
 * This class represents the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Node extends HashMap<Integer,edge_data> implements node_data {
    private int key;
    private geo_location location;
    private double weight;
    private String info;
    private int tag;


    public Node(int key, geo_location location, double weight, String info, int tag) {
        this.key = key;
        this.location = new Location(location);
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    public Node(node_data n) {
        this(n.getKey(), n.getLocation(), n.getWeight(), n.getInfo(), n.getTag());
    }

    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    @Override
    public int getKey() {
        return key;
    }

    /** Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return location;
    }

    /** Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.location = new Location(p);
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return double
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     * @return String
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return int
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * Returns the vertex as a String
     *
     * @return String
     */
    public String toString() {
        return "{" + getKey() + ","
                + "[" + getLocation().toString() + "],"
                + getWeight() + ","
                + getInfo() + ","
                + getTag()
                + "}";
    }

    /**
     * Equals method
     *
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj){
        if(!(obj instanceof node_data))return false;
        node_data n=(node_data)obj;
        return getKey()==n.getKey()&&getLocation().equals(n.getLocation());
    }
}