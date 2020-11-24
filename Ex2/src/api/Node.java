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
    /////////// PRIVATE CLASS //////////
    private class Location implements geo_location{
        private double x;
        private double y;
        private double z;

        public Location(double x,double y,double z){this.x=x;this.y=y;this.z=z;}
        public Location (geo_location l){this(l.x(),l.y(),l.z());}
        @Override
        public double x() {return x;}
        @Override
        public double y() {return y;}
        @Override
        public double z() {return z;}
        @Override
        public double distance(geo_location g) {
            double dx=x()-g.x(),dy=y()-g.y(),dz=z()-g.z();
            double p=(dx*dx+dy*dy+dz*dz);
            return Math.sqrt(p);
        }
    }
}