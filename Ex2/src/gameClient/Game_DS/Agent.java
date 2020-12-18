package gameClient.Game_DS;

import api.edge_data;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

/**
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
     *
     * @param json
     * @throws JSONException
     */
    public Agent(JSONObject json) throws JSONException {
        update(json);
    }

    /**
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
     *
     * @param s
     * @return double[] - double array
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
     *
     * @return Integer
     */
    public Integer getId() {return id;}

    /**
     *
     * @return node_data
     */
    public node_data getNode() {return node;}

    /**
     *
     * @return double
     */
    public double getValue() {return value;}

    /**
     *
     * @return int
     */
    public int getSrc() {return src;}

    /**
     *
     * @return int
     */
    public int getDest() {return dest;}

    /**
     *
     * @return double
     */
    public double getSpeed() {return speed;}

    /**
     *
     * @return Point3D
     */
    public Point3D getPos() {return pos;}

    /**
     *
     * @return edge_data
     */
    public edge_data getEdge() {return edge;}

  //  public boolean isMoving(){ return dest!=-1;}

    /**
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
