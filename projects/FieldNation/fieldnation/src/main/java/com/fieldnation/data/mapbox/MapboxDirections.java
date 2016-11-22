package com.fieldnation.data.mapbox;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 6/22/2016.
 */
public class MapboxDirections implements Parcelable {
    private static final String TAG = "MapboxDirections";

    @Json(name = "code")
    private String _code;

    @Json(name = "routes")
    private MapboxRoute[] _routes;

    public MapboxDirections() {
    }

    public String getCode() {
        return _code;
    }

    public MapboxRoute[] getRoutes() {
        return _routes;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MapboxDirections workorder) {
        try {
            return Serializer.serializeObject(workorder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static MapboxDirections fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(MapboxDirections.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<MapboxDirections> CREATOR = new Creator<MapboxDirections>() {

        @Override
        public MapboxDirections createFromParcel(Parcel source) {
            try {
                return MapboxDirections.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public MapboxDirections[] newArray(int size) {
            return new MapboxDirections[size];
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
