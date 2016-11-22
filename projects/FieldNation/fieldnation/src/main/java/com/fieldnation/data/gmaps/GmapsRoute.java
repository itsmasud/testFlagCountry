package com.fieldnation.data.gmaps;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Shoaib on 10/14/2016.
 */
public class GmapsRoute implements Parcelable {
    private static final String TAG = "GmapsRoute";

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "duration")
    private Double _duration;

    public GmapsRoute() {
    }

    public double getDistance() {
        if (_distance == null)
            return 0;

        return _distance;
    }

    public double getDistanceMi() {
        if (_distance == null)
            return 0;

        return _distance * 0.000621371;
    }

    public double getDuration() {
        if (_duration == null)
            return 0;

        return _duration;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GmapsRoute workorder) {
        try {
            return Serializer.serializeObject(workorder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static GmapsRoute fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(GmapsRoute.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<GmapsRoute> CREATOR = new Creator<GmapsRoute>() {

        @Override
        public GmapsRoute createFromParcel(Parcel source) {
            try {
                return GmapsRoute.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return null;
        }

        @Override
        public GmapsRoute[] newArray(int size) {
            return new GmapsRoute[size];
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
