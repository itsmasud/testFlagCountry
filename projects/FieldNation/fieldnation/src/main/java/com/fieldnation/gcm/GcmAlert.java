package com.fieldnation.gcm;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/29/2016.
 */

public class GcmAlert implements Parcelable {
    public static final String TAG = "GcmAlert";

    @Json
    public String title;
    @Json
    public String body;

    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GcmAlert fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(GcmAlert.class, json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<GcmAlert> CREATOR = new Parcelable.Creator<GcmAlert>() {

        @Override
        public GcmAlert createFromParcel(Parcel source) {
            try {
                return GcmAlert.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GcmAlert[] newArray(int size) {
            return new GcmAlert[size];
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
