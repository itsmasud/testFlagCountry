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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class ErrorTraceArgs implements Parcelable {
    private static final String TAG = "ErrorTraceArgs";

    @Json(name = "message")
    private String _message;

    @Json(name = "status_code")
    private Integer _statusCode;

    @Source
    private JsonObject SOURCE;

    public ErrorTraceArgs() {
        SOURCE = new JsonObject();
    }

    public ErrorTraceArgs(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMessage(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
    }

    public String getMessage() {
        try {
            if (_message == null && SOURCE.has("message") && SOURCE.get("message") != null)
                _message = SOURCE.getString("message");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _message;
    }

    public ErrorTraceArgs message(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
        return this;
    }

    public void setStatusCode(Integer statusCode) throws ParseException {
        _statusCode = statusCode;
        SOURCE.put("status_code", statusCode);
    }

    public Integer getStatusCode() {
        try {
            if (_statusCode == null && SOURCE.has("status_code") && SOURCE.get("status_code") != null)
                _statusCode = SOURCE.getInt("status_code");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _statusCode;
    }

    public ErrorTraceArgs statusCode(Integer statusCode) throws ParseException {
        _statusCode = statusCode;
        SOURCE.put("status_code", statusCode);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ErrorTraceArgs[] array) {
        JsonArray list = new JsonArray();
        for (ErrorTraceArgs item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ErrorTraceArgs[] fromJsonArray(JsonArray array) {
        ErrorTraceArgs[] list = new ErrorTraceArgs[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ErrorTraceArgs fromJson(JsonObject obj) {
        try {
            return new ErrorTraceArgs(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
