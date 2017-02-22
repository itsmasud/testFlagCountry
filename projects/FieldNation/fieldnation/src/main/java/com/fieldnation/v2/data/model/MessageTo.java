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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class MessageTo implements Parcelable {
    private static final String TAG = "MessageTo";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "role")
    private String _role;

    @Json(name = "thumbnail")
    private String _thumbnail;

    @Source
    private JsonObject SOURCE;

    public MessageTo() {
        SOURCE = new JsonObject();
    }

    public MessageTo(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public MessageTo id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name != null)
                return _name;

            if (SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public MessageTo name(String name) throws ParseException {
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
            if (_role != null)
                return _role;

            if (SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public MessageTo role(String role) throws ParseException {
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
            if (_thumbnail != null)
                return _thumbnail;

            if (SOURCE.has("thumbnail") && SOURCE.get("thumbnail") != null)
                _thumbnail = SOURCE.getString("thumbnail");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _thumbnail;
    }

    public MessageTo thumbnail(String thumbnail) throws ParseException {
        _thumbnail = thumbnail;
        SOURCE.put("thumbnail", thumbnail);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(MessageTo[] array) {
        JsonArray list = new JsonArray();
        for (MessageTo item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static MessageTo[] fromJsonArray(JsonArray array) {
        MessageTo[] list = new MessageTo[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static MessageTo fromJson(JsonObject obj) {
        try {
            return new MessageTo(obj);
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
    public static final Parcelable.Creator<MessageTo> CREATOR = new Parcelable.Creator<MessageTo>() {

        @Override
        public MessageTo createFromParcel(Parcel source) {
            try {
                return MessageTo.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public MessageTo[] newArray(int size) {
            return new MessageTo[size];
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
}
