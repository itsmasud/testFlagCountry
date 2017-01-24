package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MessageFrom {
    private static final String TAG = "MessageFrom";

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "role")
    private String role;

    @Json(name = "name")
    private String name;

    @Json(name = "id")
    private Integer id;

    @Json(name = "hideWoManager")
    private Boolean hideWoManager;

    @Json(name = "msgLink")
    private String msgLink;

    public MessageFrom() {
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

    public Boolean getHideWoManager() {
        return hideWoManager;
    }

    public String getMsgLink() {
        return msgLink;
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
