package com.fieldnation.data.mapbox;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

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
    public static final Parcelable.Creator<MapboxDirections> CREATOR = new Parcelable.Creator<MapboxDirections>() {

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
