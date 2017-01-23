package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MessageFrom {
    private static final String TAG = "MessageFrom";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "thumbnail")
    private String thumbnail = null;

    @Json(name = "hideWoManager")
    private Boolean hideWoManager = null;

    @Json(name = "msgLink")
    private String msgLink = null;

    @Json(name = "role")
    private String role = null;

    public MessageFrom() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Boolean getHideWoManager() {
        return hideWoManager;
    }

    public String getMsgLink() {
        return msgLink;
    }

    public String getRole() {
        return role;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageFrom fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageFrom.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MessageFrom messageFrom) {
        try {
            return Serializer.serializeObject(messageFrom);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

