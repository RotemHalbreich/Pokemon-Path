package api;

import gameClient.util.Point3D;

/**
 * This class represents a geo location (x,y,z), aka Point3D
 */
public class Location extends Point3D implements geo_location {
    public Location(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     *
     *
     * @param g
     */
    public Location(geo_location g) {
        super(g.x(), g.y(), g.z());
    }
}