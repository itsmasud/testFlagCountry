package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MessageTo {
    private static final String TAG = "MessageTo";

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "role")
    private String role;

    @Json(name = "name")
    private String name;

    @Json(name = "id")
    private Integer id;

    public MessageTo() {
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getRole() {
        return role;
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
    public static MessageTo fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageTo.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MessageTo messageTo) {
        try {
            return Serializer.serializeObject(messageTo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
