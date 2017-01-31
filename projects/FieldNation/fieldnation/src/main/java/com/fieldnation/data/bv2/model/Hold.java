package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class Hold implements Parcelable {
    private static final String TAG = "Hold";

    @Json(name = "reason")
    private String _reason;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public Hold() {
    }

    public void setReason(String reason) {
        _reason = reason;
    }

    public String getReason() {
        return _reason;
    }

    public Hold reason(String reason) {
        _reason = reason;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Hold name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Hold id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Hold[] fromJsonArray(JsonArray array) {
        Hold[] list = new Hold[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Hold fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Hold.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Hold hold) {
        try {
            return Serializer.serializeObject(hold);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Hold> CREATOR = new Parcelable.Creator<Hold>() {

        @Override
        public Hold createFromParcel(Parcel source) {
            try {
                return Hold.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Hold[] newArray(int size) {
            return new Hold[size];
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
