package api;

import gameClient.util.Point3D;

public class Location extends Point3D implements geo_location {
    public Location(double x, double y, double z) {
        super(x, y, z);
    }

    public Location(geo_location g) {
        super(g.x(), g.y(), g.z());
    }
}