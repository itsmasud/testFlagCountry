package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Bundle {
    private static final String TAG = "Bundle";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "count")
    private Integer count = null;

    public Bundle() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getCount() {
        return count;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Bundle fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Bundle.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Bundle bundle) {
        try {
            return Serializer.serializeObject(bundle);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

