package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserPreferredGroups {
    private static final String TAG = "UserPreferredGroups";

    @Json(name = "notes")
    private String notes;

    @Json(name = "created")
    private Date created;

    @Json(name = "name")
    private String name;

    @Json(name = "id")
    private Integer id;

    public UserPreferredGroups() {
    }

    public String getNotes() {
        return notes;
    }

    public Date getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
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
