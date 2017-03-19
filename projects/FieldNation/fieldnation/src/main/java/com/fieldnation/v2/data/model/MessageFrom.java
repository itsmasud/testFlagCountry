package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    @Source
    private JsonObject SOURCE;

    public MessageFrom() {
        SOURCE = new JsonObject();
    }

    public MessageFrom(JsonObject obj) {
        SOURCE = obj;
    }

    public void setHideWoManager(Boolean hideWoManager) throws ParseException {
        _hideWoManager = hideWoManager;
        SOURCE.put("hideWoManager", hideWoManager);
    }

    public Boolean getHideWoManager() {
        try {
            if (_hideWoManager == null && SOURCE.has("hideWoManager") && SOURCE.get("hideWoManager") != null)
                _hideWoManager = SOURCE.getBoolean("hideWoManager");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hideWoManager;
    }

    public MessageFrom hideWoManager(Boolean hideWoManager) throws ParseException {
        _hideWoManager = hideWoManager;
        SOURCE.put("hideWoManager", hideWoManager);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public MessageFrom id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setMsgLink(String msgLink) throws ParseException {
        _msgLink = msgLink;
        SOURCE.put("msgLink", msgLink);
    }

    public String getMsgLink() {
        try {
            if (_msgLink == null && SOURCE.has("msgLink") && SOURCE.get("msgLink") != null)
                _msgLink = SOURCE.getString("msgLink");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _msgLink;
    }

    public MessageFrom msgLink(String msgLink) throws ParseException {
        _msgLink = msgLink;
        SOURCE.put("msgLink", msgLink);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public MessageFrom name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public MessageFrom role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setThumbnail(String thumbnail) throws ParseException {
        _thumbnail = thumbnail;
        SOURCE.put("thumbnail", thumbnail);
    }

    public String getThumbnail() {
        try {
            if (_thumbnail == null && SOURCE.has("thumbnail") && SOURCE.get("thumbnail") != null)
                _thumbnail = SOURCE.getString("thumbnail");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _thumbnail;
    }

    public MessageFrom thumbnail(String thumbnail) throws ParseException {
        _thumbnail = thumbnail;
        SOURCE.put("thumbnail", thumbnail);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(MessageFrom[] array) {
        JsonArray list = new JsonArray();
        for (MessageFrom item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static MessageFrom[] fromJsonArray(JsonArray array) {
        MessageFrom[] list = new MessageFrom[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static MessageFrom fromJson(JsonObject obj) {
        try {
            return new MessageFrom(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}
