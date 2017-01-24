package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Users {
    private static final String TAG = "Users";

    @Json(name = "metadata")
    private ListEnvelope metadata;

    @Json(name = "results")
    private User[] results;

    public Users() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public User[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Users fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Users.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Users users) {
        try {
            return Serializer.serializeObject(users);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
