package api;

public  class Edge implements edge_data {

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

}