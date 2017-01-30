package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class MessageTo implements Parcelable {
    private static final String TAG = "MessageTo";

    @Json(name = "thumbnail")
    private String _thumbnail;

    @Json(name = "role")
    private String _role;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public MessageTo() {
    }

    public void setThumbnail(String thumbnail) {
        _thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public MessageTo thumbnail(String thumbnail) {
        _thumbnail = thumbnail;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public MessageTo role(String role) {
        _role = role;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public MessageTo name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public MessageTo id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageTo fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageTo.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
