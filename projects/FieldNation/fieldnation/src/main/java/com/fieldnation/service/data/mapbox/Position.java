package com.fieldnation.service.data.mapbox;

/**
 * Created by Michael on 6/22/2016.
 */
public class Position {

    public double longitude;
    public double latitude;

    public Position(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return longitude + "," + latitude;
    }
}
