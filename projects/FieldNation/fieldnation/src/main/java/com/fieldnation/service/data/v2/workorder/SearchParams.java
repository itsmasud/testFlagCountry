package com.fieldnation.service.data.v2.workorder;

/**
 * Created by Michael on 7/21/2016.
 */
public class SearchParams {
    private static final String TAG = "SearchParams";
    
    public String status = "available";
    public Double latitude = null;
    public Double longitude = null;
    public Double radius = null;

    public SearchParams() {
    }

    public SearchParams status(String status) {
        this.status = status;
        return this;
    }

    public SearchParams location(Double latitude, Double longitude, Double radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        return this;
    }

    public String toUrlParams() {
        String params = "?status=" + status;

        if (latitude != null && longitude != null && radius != null) {
            params += "&lat=" + latitude + "&lng=" + longitude + "&radius=" + radius;
        }

        return params;
    }

    public String toKey() {
        String key = status;

        if (latitude != null && longitude != null && radius != null) {
            key += ":" + ((int) (latitude * 1000))
                    + ":" + ((int) (longitude * 1000))
                    + ":" + ((int) (radius * 1000));
        }
        return key;
    }
}
