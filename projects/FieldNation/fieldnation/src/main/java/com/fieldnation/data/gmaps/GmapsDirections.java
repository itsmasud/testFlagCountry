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
public class GmapsDirections implements Parcelable {
    private static final String TAG = "GmapsDirections";

    @Json(name = "code")
    private String _code;

    @Json(name = "routes")
    private GmapsRoute[] _routes;

    public GmapsDirections() {
    }

    public String getCode() {
        return _code;
    }

    public GmapsRoute[] getRoutes() {
        return _routes;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GmapsDirections workorder) {
        try {
            return Serializer.serializeObject(workorder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static GmapsDirections fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(GmapsDirections.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<GmapsDirections> CREATOR = new Creator<GmapsDirections>() {

        @Override
        public GmapsDirections createFromParcel(Parcel source) {
            try {
                return GmapsDirections.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GmapsDirections[] newArray(int size) {
            return new GmapsDirections[size];
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
