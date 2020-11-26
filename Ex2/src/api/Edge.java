package api;

import java.util.Objects;

public  class Edge implements edge_data{

    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public Edge (int src,int dest,double weight,String info,int tag){
        this.src=src;
        this.dest=dest;
        this.weight=weight;
        this.info=info;
        this.tag=tag;
    }
    public Edge(int src ,int dest,double weight){
        this(src,dest,weight,"",0);
    }
    public Edge(edge_data e){
        this(e.getSrc(),e.getDest(),e.getWeight());
    }
    @Override
    public int getSrc() {return src;}

    @Override
    public int getDest() {return dest;}

    @Override
    public double getWeight() {return weight;}

    @Override
    public String getInfo() {return info;}

    @Override
    public void setInfo(String s) {this.info=s;}

    @Override
    public int getTag() {return tag;}

    @Override
    public void setTag(int t) {this.tag=t;}
    public String toString(){
        return "("+getSrc()+","+getDest()+"|"+getWeight()+")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight, info, tag);
    }

    public boolean equals(Object obj){
        if(!(obj instanceof edge_data))return false;
        edge_data e=(edge_data)obj;
        return getSrc()==e.getSrc()&&
                getDest()==e.getDest()&&
                getWeight()==e.getWeight();
    }
}