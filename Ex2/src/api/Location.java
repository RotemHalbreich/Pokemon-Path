package api;

import gameClient.util.Point3D;

public  class Location extends Point3D implements geo_location{
    public Location(double x, double y, double z) {
        super(x, y, z);
    }
    public Location(geo_location g){
        super(g.x(), g.y(), g.z());
    }
//    private double x;
//    private double y;
//    private double z;
//
//    public Location(double x,double y,double z){this.x=x;this.y=y;this.z=z;}
//    public Location (geo_location l){this(l.x(),l.y(),l.z());}
//    @Override
//    public double x() {return x;}
//    @Override
//    public double y() {return y;}
//    @Override
//    public double z() {return z;}
//    @Override
//    public double distance(geo_location g) {
//        double dx=x()-g.x(),dy=y()-g.y(),dz=z()-g.z();
//        double p=(dx*dx+dy*dy+dz*dz);
//        return Math.sqrt(p);
//    }
}