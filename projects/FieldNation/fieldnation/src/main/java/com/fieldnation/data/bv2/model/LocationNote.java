package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationNote {
    private static final String TAG = "LocationNote";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "text")
    private String text = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "private")
    private Boolean _private = null;

    @Json(name = "user_id")
    private Integer userId = null;

    public LocationNote() {
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getCreated() {
        return created;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public Integer getUserId() {
        return userId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationNote fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationNote.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationNote locationNote) {
        try {
            return Serializer.serializeObject(locationNote);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

