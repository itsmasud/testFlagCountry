package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class ErrorTraceObject implements Parcelable {
    private static final String TAG = "ErrorTraceObject";

    @Source
    private JsonObject SOURCE;

    public ErrorTraceObject() {
        SOURCE = new JsonObject();
    }

    public ErrorTraceObject(JsonObject obj) {
        SOURCE = obj;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ErrorTraceObject[] array) {
        JsonArray list = new JsonArray();
        for (ErrorTraceObject item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ErrorTraceObject[] fromJsonArray(JsonArray array) {
        ErrorTraceObject[] list = new ErrorTraceObject[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ErrorTraceObject fromJson(JsonObject obj) {
        try {
            return new ErrorTraceObject(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
