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

public class GcmMessage implements Parcelable {
    public static final String TAG = "GcmMessage";

    @Json
    public String title;
    @Json
    public String body;
    @Json
    public String sound;
    @Json
    public Integer badge;
    @Json
    public String category;
    @Json
    public Action[] actions;

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GcmMessage schedule) {
        try {
            return Serializer.serializeObject(schedule);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static GcmMessage fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(GcmMessage.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<GcmMessage> CREATOR = new Parcelable.Creator<GcmMessage>() {

        @Override
        public GcmMessage createFromParcel(Parcel source) {
            try {
                return GcmMessage.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GcmMessage[] newArray(int size) {
            return new GcmMessage[size];
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
