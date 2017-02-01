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

public class Status implements Parcelable {
    private static final String TAG = "Status";

    @Json(name = "display")
    private String _display;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "publish_stats")
    private StatusPublishStats _publishStats;

    public Status() {
    }

    public void setDisplay(String display) {
        _display = display;
    }

    public String getDisplay() {
        return _display;
    }

    public Status display(String display) {
        _display = display;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Status name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Status id(Integer id) {
        _id = id;
        return this;
    }

    public void setPublishStats(StatusPublishStats publishStats) {
        _publishStats = publishStats;
    }

    public StatusPublishStats getPublishStats() {
        return _publishStats;
    }

    public Status publishStats(StatusPublishStats publishStats) {
        _publishStats = publishStats;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Status[] fromJsonArray(JsonArray array) {
        Status[] list = new Status[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Status fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Status.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Status status) {
        try {
            return Serializer.serializeObject(status);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {

        @Override
        public Status createFromParcel(Parcel source) {
            try {
                return Status.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
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
