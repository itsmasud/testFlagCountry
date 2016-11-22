package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 7/21/2016.
 */
public class Schedule implements Parcelable {
    private static final String TAG = "Schedule";

    @Json
    private Estimate estimate;
    @Json
    private Range range;
    @Json
    private String exact;

    public Schedule() {
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public Range getRange() {
        return range;
    }

    public String getExact() {
        return exact;
    }

    public void setExact(String exact) {
        this.exact = exact;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public String getBegin() {
        if (getEstimate() != null && getEstimate().getArrival() != null)
            return getEstimate().getArrival();
        else if (getRange() != null)
            return getRange().getBegin();
        else if (getExact() != null)
            return getExact();

        return null;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Schedule schedule) {
        try {
            return Serializer.serializeObject(schedule);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Schedule fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Schedule.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {

        @Override
        public Schedule createFromParcel(Parcel source) {
            try {
                return Schedule.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
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
