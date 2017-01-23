package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserPreferredGroups {
    private static final String TAG = "UserPreferredGroups";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "notes")
    private String notes = null;

    @Json(name = "created")
    private String created = null;

    public UserPreferredGroups() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreated() {
        return created;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserPreferredGroups fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserPreferredGroups.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserPreferredGroups userPreferredGroups) {
        try {
            return Serializer.serializeObject(userPreferredGroups);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}