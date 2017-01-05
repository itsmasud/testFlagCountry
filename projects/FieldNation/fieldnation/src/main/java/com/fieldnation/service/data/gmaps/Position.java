package com.fieldnation.service.data.gmaps;

/**
 * Created by Shoaib on 10/21/2016.
 */
public class Position {

    public final double latitude;
    public final double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

    public static final double DEG2RAD = (double) (Math.PI / 180.0);
    public static final double RAD2DEG = (double) (180.0 / Math.PI);
    public static final double PI = (double) Math.PI;
    public static final double RADIUS_EARTH_METERS = 6372800;
    public static final double RADIUS_METERS_TO_MILES = 0.000621371D;
    public static final double RADIUS_EARTH_MILES = 3959.8731088D;

    public int distanceTo(final Position other) {
        return (int) haversine(latitude, longitude, other.latitude, other.longitude);
    }

    // Source: https://rosettacode.org/wiki/Haversine_formula#Java
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return RADIUS_EARTH_MILES * c;
    }
}
