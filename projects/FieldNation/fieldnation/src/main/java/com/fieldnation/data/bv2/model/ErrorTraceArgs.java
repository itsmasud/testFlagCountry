package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class ErrorTraceArgs implements Parcelable {
    private static final String TAG = "ErrorTraceArgs";

    @Json(name = "status_code")
    private Integer _statusCode;

    @Json(name = "message")
    private String _message;

    public ErrorTraceArgs() {
    }

    public void setStatusCode(Integer statusCode) {
        _statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return _statusCode;
    }

    public ErrorTraceArgs statusCode(Integer statusCode) {
        _statusCode = statusCode;
        return this;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public ErrorTraceArgs message(String message) {
        _message = message;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ErrorTraceArgs[] fromJsonArray(JsonArray array) {
        ErrorTraceArgs[] list = new ErrorTraceArgs[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ErrorTraceArgs fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ErrorTraceArgs.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ErrorTraceArgs errorTraceArgs) {
        try {
            return Serializer.serializeObject(errorTraceArgs);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ErrorTraceArgs> CREATOR = new Parcelable.Creator<ErrorTraceArgs>() {

        @Override
        public ErrorTraceArgs createFromParcel(Parcel source) {
            try {
                return ErrorTraceArgs.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ErrorTraceArgs[] newArray(int size) {
            return new ErrorTraceArgs[size];
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
