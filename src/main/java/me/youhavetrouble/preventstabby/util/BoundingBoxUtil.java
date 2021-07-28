package me.youhavetrouble.preventstabby.util;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class BoundingBoxUtil {

    public static BoundingBox getBoundingBox(Location location, double radius) {

        double x1 = location.getX()+radius;
        double y1 = location.getY()+radius;
        double z1 = location.getZ()+radius;

        double x2 = location.getX()-radius;
        double y2 = location.getY()-radius;
        double z2 = location.getZ()-radius;

        return new BoundingBox(x1, y1, z1, x2, y2, z2);

    }

}
