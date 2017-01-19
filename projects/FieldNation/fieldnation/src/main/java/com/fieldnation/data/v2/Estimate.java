package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 8/19/2016.
 */
public class Estimate implements Parcelable {
    private static final String TAG = "Estimate";

    @Json
    private String arrival;
    @Json
    private Double _duration;

    public Estimate() {
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public Double getDuration() {
        return _duration;
    }

    public void setDuration(Double duration) {
        this._duration = duration;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Estimate estimate) {
        try {
            return Serializer.serializeObject(estimate);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Estimate fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Estimate.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Estimate> CREATOR = new Parcelable.Creator<Estimate>() {

        @Override
        public Estimate createFromParcel(Parcel source) {
            try {
                return Estimate.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Estimate[] newArray(int size) {
            return new Estimate[size];
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
