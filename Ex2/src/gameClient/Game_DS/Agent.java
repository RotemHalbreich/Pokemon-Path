package gameClient.Game_DS;

import api.edge_data;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

/**
 * Represents an Agent in the game.
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

public class Agent {

    private Integer id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private Point3D pos;
    private edge_data edge;
    private node_data node;

    /**
     * Constructor:
     *
     * @param json
     * @throws JSONException
     */
    public Agent(JSONObject json) throws JSONException {
        update(json);
    }

    /**
     * Updates the game's Agent.
     *
     * @param json
     * @throws JSONException
     */
    public void update(JSONObject json) throws JSONException {
        id= json.getInt("id");
        src= json.getInt("src");
        dest= json.getInt("dest");
        value= json.getDouble("value");
        speed= json.getDouble("speed");
        double[] c=simplifyLocation(json.getString("pos"));
        pos=new Point3D(c[0],c[1],c[2]);
        //edge->we need think
        //node->we need think
    }

    /**
     * Simplifies the agent's location.
     *
     * @param s
     * @return double[]
     */
    private double[] simplifyLocation(String s){
        double[] ans=new double[3];
        String[] as=s.split(",");
        ans[0]=parseDouble(as[0]);
        ans[1]=parseDouble(as[1]);
        ans[2]=parseDouble(as[2]);
        return ans;
    }

    /**
     * Gets the agent's ID.
     *
     * @return Integer
     */
    public Integer getId() {return id;}

    /**
     * Gets the agent's vertex.
     *
     * @return node_data
     */
    public node_data getNode() {return node;}

    /**
     * Gets the agent's value.
     *
     * @return double
     */
    public double getValue() {return value;}

    /**
     * Gets the agent's source.
     *
     * @return int
     */
    public int getSrc() {return src;}

    /**
     * Gets the agent's destination.
     *
     * @return int
     */
    public int getDest() {return dest;}

    /**
     * Gets the agent's speed.
     *
     * @return double
     */
    public double getSpeed() {return speed;}

    /**
     * Gets the agent's position.
     *
     * @return Point3D
     */
    public Point3D getPos() {return pos;}

    /**
     * Gets the agent's edge.
     *
     * @return edge_data
     */
    public edge_data getEdge() {return edge;}

  //  public boolean isMoving(){ return dest!=-1;}

    /**
     * Returns the agent as a String.
     *
     * @return String
     */
    public String toString(){
        return "Agent:{"
                +"id:"+getId()
                +",src:"+getSrc()
                +",dest:"+getDest()
                +",pos:"+getPos().toString()
                +"}";
    }
}
