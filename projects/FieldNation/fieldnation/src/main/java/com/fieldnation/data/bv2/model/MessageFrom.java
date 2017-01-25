package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MessageFrom {
    private static final String TAG = "MessageFrom";

    @Json(name = "thumbnail")
    private String _thumbnail;

    @Json(name = "role")
    private String _role;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "hideWoManager")
    private Boolean _hideWoManager;

    @Json(name = "msgLink")
    private String _msgLink;

    public MessageFrom() {
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public String getRole() {
        return _role;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    public Boolean getHideWoManager() {
        return _hideWoManager;
    }

    public String getMsgLink() {
        return _msgLink;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageFrom fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageFrom.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
