package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Countries {
    private static final String TAG = "Countries";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Country[] _results;

    public Countries() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Country[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Countries fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Countries.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Countries countries) {
        try {
            return Serializer.serializeObject(countries);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
