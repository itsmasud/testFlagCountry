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
 * Created by dmgen from swagger on 1/31/17.
 */

public class Error implements Parcelable {
    private static final String TAG = "Error";

    @Json(name = "trace")
    private ErrorTrace[] _trace;

    @Json(name = "code")
    private Integer _code;

    @Json(name = "message")
    private String _message;

    @Json(name = "fields")
    private String _fields;

    public Error() {
    }

    public void setTrace(ErrorTrace[] trace) {
        _trace = trace;
    }

    public ErrorTrace[] getTrace() {
        return _trace;
    }

    public Error trace(ErrorTrace[] trace) {
        _trace = trace;
        return this;
    }

    public void setCode(Integer code) {
        _code = code;
    }

    public Integer getCode() {
        return _code;
    }

    public Error code(Integer code) {
        _code = code;
        return this;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public Error message(String message) {
        _message = message;
        return this;
    }

    public void setFields(String fields) {
        _fields = fields;
    }

    public String getFields() {
        return _fields;
    }

    public Error fields(String fields) {
        _fields = fields;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Error[] fromJsonArray(JsonArray array) {
        Error[] list = new Error[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Error fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Error.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Error error) {
        try {
            return Serializer.serializeObject(error);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Error> CREATOR = new Parcelable.Creator<Error>() {

        @Override
        public Error createFromParcel(Parcel source) {
            try {
                return Error.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Error[] newArray(int size) {
            return new Error[size];
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
