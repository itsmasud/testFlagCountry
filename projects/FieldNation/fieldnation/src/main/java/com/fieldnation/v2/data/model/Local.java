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
 * Created by dmgen from swagger on 1/31/17.
 */

public class Local implements Parcelable {
    private static final String TAG = "Local";

    @Json(name = "date")
    private String _date;

    @Json(name = "time")
    private String _time;

    public Local() {
    }

    public void setDate(String date) {
        _date = date;
    }

    public String getDate() {
        return _date;
    }

    public Local date(String date) {
        _date = date;
        return this;
    }

    public void setTime(String time) {
        _time = time;
    }

    public String getTime() {
        return _time;
    }

    public Local time(String time) {
        _time = time;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Local[] fromJsonArray(JsonArray array) {
        Local[] list = new Local[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Local fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Local.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Local local) {
        try {
            return Serializer.serializeObject(local);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Local> CREATOR = new Parcelable.Creator<Local>() {

        @Override
        public Local createFromParcel(Parcel source) {
            try {
                return Local.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
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
