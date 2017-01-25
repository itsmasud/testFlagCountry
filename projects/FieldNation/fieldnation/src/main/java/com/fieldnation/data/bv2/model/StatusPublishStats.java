package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class StatusPublishStats {
    private static final String TAG = "StatusPublishStats";

    @Json(name = "routes")
    private Integer _routes;

    @Json(name = "counter_offers")
    private Integer _counterOffers;

    @Json(name = "requests")
    private Integer _requests;

    public StatusPublishStats() {
    }

    public Integer getRoutes() {
        return _routes;
    }

    public Integer getCounterOffers() {
        return _counterOffers;
    }

    public Integer getRequests() {
        return _requests;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static StatusPublishStats fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(StatusPublishStats.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(StatusPublishStats statusPublishStats) {
        try {
            return Serializer.serializeObject(statusPublishStats);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
