package com.fieldnation.service.data.gmaps;

import com.fieldnation.fntools.misc;

/**
 * Created by Shoaib on 10/14/2016.
 */
public class Marker extends Position {

    private final String _url;

    public Marker(double longitude, double latitude, String url) {
        super(longitude, latitude);
        this._url = url;
    }

    @Override
    public String toString() {
        return "url-" + misc.escapeForURL(_url) + "(" + super.toString() + ")";
    }
}
