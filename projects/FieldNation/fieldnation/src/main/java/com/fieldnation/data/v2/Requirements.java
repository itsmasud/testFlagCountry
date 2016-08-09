package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by Michael on 7/21/2016.
 */
public class Requirements implements Parcelable {
    private static final String TAG = "Requirements";

    @Json
    private Schedule schedule;
    @Json
    private Boolean gps_checkin;

    public Requirements() {
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Boolean getGpsCheckin() {
        return gps_checkin;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Requirements requirements) {
        try {
            return Serializer.serializeObject(requirements);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Requirements fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Requirements.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Requirements> CREATOR = new Parcelable.Creator<Requirements>() {

        @Override
        public Requirements createFromParcel(Parcel source) {
            try {
                return Requirements.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Requirements[] newArray(int size) {
            return new Requirements[size];
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
