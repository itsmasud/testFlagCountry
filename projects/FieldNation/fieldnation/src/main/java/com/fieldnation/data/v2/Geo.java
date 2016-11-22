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
public class Geo implements Parcelable {
    private static final String TAG = "Geo";

    @Json
    private Boolean obfuscated;
    @Json
    private Double latitude;
    @Json
    private Double longitude;
    @Json
    private Boolean precise;

    public Geo() {
    }

    public Boolean getObfuscated() {
        return obfuscated;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Boolean getPrecise() {
        return precise;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Geo geo) {
        try {
            return Serializer.serializeObject(geo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Geo fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Geo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Geo> CREATOR = new Parcelable.Creator<Geo>() {

        @Override
        public Geo createFromParcel(Parcel source) {
            try {
                return Geo.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Geo[] newArray(int size) {
            return new Geo[size];
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
