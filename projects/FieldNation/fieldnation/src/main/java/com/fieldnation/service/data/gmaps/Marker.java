package com.fieldnation.service.data.gmaps;

import com.fieldnation.fntools.misc;

/**
 * Created by Shoaib on 10/14/2016.
 */
public class Marker extends Position {

    // result: markers=color:red%7C%7C23.8759457,90.3839746

    private final String _markerParam;

    public Marker(double latitude, double longitude, String url) {
        super(latitude, longitude);
        this._markerParam = url;
        //_iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "markers=" + misc.escapeForURL(_markerParam) + "|" + super.toString();
    }
}
