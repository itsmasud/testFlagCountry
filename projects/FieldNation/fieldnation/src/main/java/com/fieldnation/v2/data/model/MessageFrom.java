package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class MessageFrom implements Parcelable {
    private static final String TAG = "MessageFrom";

    @Json(name = "hideWoManager")
    private Boolean _hideWoManager;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "msgLink")
    private String _msgLink;

    @Json(name = "name")
    private String _name;

    @Json(name = "role")
    private String _role;

    @Json(name = "thumbnail")
    private String _thumbnail;

    public MessageFrom() {
    }

    public void setHideWoManager(Boolean hideWoManager) {
        _hideWoManager = hideWoManager;
    }

    public Boolean getHideWoManager() {
        return _hideWoManager;
    }

    public MessageFrom hideWoManager(Boolean hideWoManager) {
        _hideWoManager = hideWoManager;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public MessageFrom id(Integer id) {
        _id = id;
        return this;
    }

    public void setMsgLink(String msgLink) {
        _msgLink = msgLink;
    }

    public String getMsgLink() {
        return _msgLink;
    }

    public MessageFrom msgLink(String msgLink) {
        _msgLink = msgLink;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public MessageFrom name(String name) {
        _name = name;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public MessageFrom role(String role) {
        _role = role;
        return this;
    }

    public void setThumbnail(String thumbnail) {
        _thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public MessageFrom thumbnail(String thumbnail) {
        _thumbnail = thumbnail;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageFrom[] fromJsonArray(JsonArray array) {
        MessageFrom[] list = new MessageFrom[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<MessageFrom> CREATOR = new Parcelable.Creator<MessageFrom>() {

        @Override
        public MessageFrom createFromParcel(Parcel source) {
            try {
                return MessageFrom.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public MessageFrom[] newArray(int size) {
            return new MessageFrom[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
