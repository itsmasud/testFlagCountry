package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class ErrorTraceObject implements Parcelable {
    private static final String TAG = "ErrorTraceObject";

    public ErrorTraceObject() {
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ErrorTraceObject[] fromJsonArray(JsonArray array) {
        ErrorTraceObject[] list = new ErrorTraceObject[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ErrorTraceObject fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ErrorTraceObject.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ErrorTraceObject errorTraceObject) {
        try {
            return Serializer.serializeObject(errorTraceObject);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ErrorTraceObject> CREATOR = new Parcelable.Creator<ErrorTraceObject>() {

        @Override
        public ErrorTraceObject createFromParcel(Parcel source) {
            try {
                return ErrorTraceObject.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ErrorTraceObject[] newArray(int size) {
            return new ErrorTraceObject[size];
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
