package com.fieldnation.service.data.mapbox;

/**
 * Created by Michael on 6/22/2016.
 */
public class Position {

    public final double longitude;
    public final double latitude;

    public Position(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return longitude + "," + latitude;
    }

    public static final float DEG2RAD = (float) (Math.PI / 180.0);
    public static final float RAD2DEG = (float) (180.0 / Math.PI);
    public static final float PI = (float) Math.PI;
    public static final float RADIUS_EARTH_MILES = 3963.1676F;

    public int distanceTo(final Position other) {

        final double a1 = DEG2RAD * this.latitude;
        final double a2 = DEG2RAD * this.longitude;
        final double b1 = DEG2RAD * other.latitude;
        final double b2 = DEG2RAD * other.longitude;

        final double cosa1 = Math.cos(a1);
        final double cosb1 = Math.cos(b1);

        final double t1 = cosa1 * Math.cos(a2) * cosb1 * Math.cos(b2);
        final double t2 = cosa1 * Math.sin(a2) * cosb1 * Math.sin(b2);
        final double t3 = Math.sin(a1) * Math.sin(b1);
        final double tt = Math.acos(t1 + t2 + t3);

        return (int) (RADIUS_EARTH_MILES * tt);
    }
}
