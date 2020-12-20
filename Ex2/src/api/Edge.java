package api;

import java.util.Objects;

/**
 * This class represents the set of operations applicable on a
 * directional edge in a directed weighted graph.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Edge implements edge_data {

    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    /**
     * Constructor:
     *
     * @param src
     * @param dest
     * @param weight
     * @param info
     * @param tag
     */
    public Edge(int src, int dest, double weight, String info, int tag) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    /**\
     * Constructor that gets two vertices and weight.
     *
     * @param src
     * @param dest
     * @param weight
     */
    public Edge(int src, int dest, double weight) {
        this(src, dest, weight, "", 0);
    }

    /**
     * Copy constructor:
     *
     * @param e
     */
    public Edge(edge_data e) {
        this(e.getSrc(), e.getDest(), e.getWeight());
    }

    /**
     * Returns the unique key of the source vertex of this edge.
     *
     * @return int
     */
    @Override
    public int getSrc() {
        return src;
    }

    /**
     * Returns the unique key of the destination vertex of this edge.
     *
     * @return int
     */
    @Override
    public int getDest() {
        return dest;
    }

    /**
     * Returns the weight of this edge. (weight > 0)
     *
     * @return double
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the information associated with this edge.
     *
     * @return String
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the information associated with this edge.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Returns the "tag" data of this edge which can be used in
     * the algorithms of the graph.
     *
     * @return int
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * Allows setting the "tag" value for an edge.
     * 
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * Returns the edge as a String.
     *
     * @return String
     */
    public String toString() {
        return "(" + getSrc() + "," + getDest() + "|" + getWeight() + ")";
    }

    /**
     * hashCode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight, info, tag);
    }

    /**
     * Checks if two edges are equal.
     *
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof edge_data)) return false;
        edge_data e = (edge_data) obj;
        return getSrc() == e.getSrc() &&
                getDest() == e.getDest() &&
                getWeight() == e.getWeight();
    }
}