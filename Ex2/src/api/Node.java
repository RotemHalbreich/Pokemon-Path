package api;

import java.util.HashMap;

public class Node extends HashMap<Integer,edge_data> implements node_data{
    private int key;
    private geo_location location;
    private double weight;
    private String info;
    private int tag;

    public Node(int key,geo_location location,double weight,String info,int tag){
        this.key=key;
        this.location=new Location(location);
        this.weight=weight;
        this.info=info;
        this.tag=tag;
    }
    public Node(node_data n){this(n.getKey(),n.getLocation(),n.getWeight(),n.getInfo(),n.getTag());}
    @Override
    public int getKey() {return key;}
    @Override
    public geo_location getLocation() {return location;}

    @Override
    public void setLocation(geo_location p) {this.location=new Location(p);}
    @Override
    public double getWeight() {return weight;}
    @Override
    public void setWeight(double w) {this.weight=w;}

    @Override
    public String getInfo() {return info;}

    @Override
    public void setInfo(String s) {this.info=s;}

    @Override
    public int getTag() {return tag;}

    @Override
    public void setTag(int t) {this.tag=t;}
    public String toString(){
        return "{"+getKey()+","
                +"["+getLocation().toString()+"],"
                +getWeight()+","
                +getInfo()+","
                +getTag()
                +"}";
    }


}