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

public class Error implements Parcelable {
    private static final String TAG = "Error";

    @Json(name = "code")
    private Integer _code;

    @Json(name = "fields")
    private String _fields;

    @Json(name = "message")
    private String _message;

    @Json(name = "trace")
    private ErrorTrace[] _trace;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Error() {
    }

    public void setCode(Integer code) throws ParseException {
        _code = code;
        SOURCE.put("code", code);
    }

    public Integer getCode() {
        return _code;
    }

    public Error code(Integer code) throws ParseException {
        _code = code;
        SOURCE.put("code", code);
        return this;
    }

    public void setFields(String fields) throws ParseException {
        _fields = fields;
        SOURCE.put("fields", fields);
    }

    public String getFields() {
        return _fields;
    }

    public Error fields(String fields) throws ParseException {
        _fields = fields;
        SOURCE.put("fields", fields);
        return this;
    }

    public void setMessage(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
    }

    public String getMessage() {
        return _message;
    }

    public Error message(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
        return this;
    }

    public void setTrace(ErrorTrace[] trace) throws ParseException {
        _trace = trace;
        SOURCE.put("trace", ErrorTrace.toJsonArray(trace));
    }

    public ErrorTrace[] getTrace() {
        return _trace;
    }

    public Error trace(ErrorTrace[] trace) throws ParseException {
        _trace = trace;
        SOURCE.put("trace", ErrorTrace.toJsonArray(trace), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Error[] array) {
        JsonArray list = new JsonArray();
        for (Error item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
