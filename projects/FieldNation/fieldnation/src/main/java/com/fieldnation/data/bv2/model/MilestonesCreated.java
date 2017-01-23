package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MilestonesCreated {
    private static final String TAG = "MilestonesCreated";

    @Json(name = "utc")
    private String utc = null;

    @Json(name = "local")
    private MilestonesCreatedLocal local = null;

    public MilestonesCreated() {
    }

    public String getUtc() {
        return utc;
    }

    public MilestonesCreatedLocal getLocal() {
        return local;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MilestonesCreated fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MilestonesCreated.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MilestonesCreated milestonesCreated) {
        try {
            return Serializer.serializeObject(milestonesCreated);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}